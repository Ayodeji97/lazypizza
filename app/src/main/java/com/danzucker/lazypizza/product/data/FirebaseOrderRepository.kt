package com.danzucker.lazypizza.product.data

import com.danzucker.lazypizza.auth.data.AuthManager
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.model.Order
import com.danzucker.lazypizza.product.domain.model.OrderStatus
import com.danzucker.lazypizza.product.domain.order.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private const val ORDERS_COLLECTION = "orders"
private const val USERS_COLLECTION = "users"
private const val USER_ORDERS_COLLECTION = "orders"

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseOrderRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val authManager: AuthManager = AuthManager()
) : OrderRepository {

    /**
     * Create a new order with transaction safety
     *
     * Strategy:
     * 1. Create order in main /orders collection
     * 2. Create reference in /users/{userId}/orders
     * 3. Both operations in a batch write for atomicity
     */
    override suspend fun createOrder(order: Order): Result<String, DataError.Network> {
        return try {
            val userId = authManager.currentUserId

            if (userId == null) {
                Timber.e("Cannot create order: user not authenticated")
                return Result.Error(DataError.Network.UNAUTHORIZED)
            }

            // Validate order
            if (order.items.isEmpty()) {
                Timber.e("Cannot create order: no items")
                return Result.Error(DataError.Network.BAD_REQUEST)
            }

            val orderId = firestore.collection(ORDERS_COLLECTION).document().id
            val orderWithId = order.copy(
                id = orderId,
                userId = userId
            )

            // Use batch write for atomicity
            val batch = firestore.batch()

            // 1. Create in main orders collection
            val mainOrderRef = firestore
                .collection(ORDERS_COLLECTION)
                .document(orderId)
            batch.set(mainOrderRef, orderWithId.toFirestoreMap())

            // 2. Create reference in user's orders subcollection
            val userOrderRef = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(USER_ORDERS_COLLECTION)
                .document(orderId)
            batch.set(userOrderRef, orderWithId.toFirestoreMap())

            // Commit batch
            batch.commit().await()

            Timber.d("Order created successfully: $orderId")
            Result.Success(orderId)

        } catch (e: Exception) {
            Timber.e(e, "Failed to create order")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    /**
     * Get all orders for the current user with real-time updates
     * Orders are sorted by creation date (newest first)
     */
    override fun getOrders(): Flow<Result<List<Order>, DataError.Network>> {
        return authManager.observeAuthState()
            .distinctUntilChanged { old, new -> old?.uid == new?.uid }
            .flatMapLatest { user ->
                if (user == null || user.isAnonymous) {
                    Timber.d("No authenticated user, returning empty orders")
                    flowOf(Result.Success(emptyList()))
                } else {
                    Timber.d("Observing orders for user: ${user.uid}")
                    observeOrdersForUser(user.uid)
                }
            }
    }


    private fun observeOrdersForUser(userId: String): Flow<Result<List<Order>, DataError.Network>> = callbackFlow {
        val listener = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(USER_ORDERS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Error observing orders for user $userId")
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                    return@addSnapshotListener
                }

                val orders = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Order.fromFirestoreMap(doc.data ?: emptyMap())
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing order document: ${doc.id}")
                        null
                    }
                } ?: emptyList()

                Timber.d("Orders updated: ${orders.size} orders")
                trySend(Result.Success(orders))
            }

        awaitClose {
            Timber.d("Closing orders observer for user $userId")
            listener.remove()
        }
    }

    /**
     * Get a specific order by ID (one-time fetch)
     */
    override suspend fun getOrderById(orderId: String): Result<Order?, DataError.Network> {
        return try {
            val doc = firestore
                .collection(ORDERS_COLLECTION)
                .document(orderId)
                .get()
                .await()

            if (!doc.exists()) {
                Timber.w("Order not found: $orderId")
                return Result.Success(null)
            }

            val order = Order.fromFirestoreMap(doc.data ?: emptyMap())
            Result.Success(order)

        } catch (e: Exception) {
            Timber.e(e, "Failed to get order: $orderId")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    /**
     * Update order status
     * Updates both main collection and user subcollection
     */
    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ): EmptyResult<DataError.Network> {
        return try {
            val userId = authManager.currentUserId
            if (userId == null) {
                Timber.e("Cannot update order status: user not authenticated")
                return Result.Error(DataError.Network.UNAUTHORIZED)
            }

            val batch = firestore.batch()
            val now = System.currentTimeMillis()

            val updates = mapOf(
                "status" to status.name,
                "updatedAt" to now
            )

            // Update main orders collection
            val mainOrderRef = firestore
                .collection(ORDERS_COLLECTION)
                .document(orderId)
            batch.update(mainOrderRef, updates)

            // Update user orders subcollection
            val userOrderRef = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(USER_ORDERS_COLLECTION)
                .document(orderId)
            batch.update(userOrderRef, updates)

            batch.commit().await()

            Timber.d("Order status updated: $orderId -> $status")
            Result.Success(Unit)

        } catch (e: Exception) {
            Timber.e(e, "Failed to update order status: $orderId")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    /**
     * Cancel an order
     */
    override suspend fun cancelOrder(orderId: String): EmptyResult<DataError.Network> {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }
}