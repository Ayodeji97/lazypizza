@file:OptIn(ExperimentalCoroutinesApi::class)

package com.danzucker.lazypizza.product.data

import com.danzucker.lazypizza.auth.data.AuthManager
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.CartSummary
import com.danzucker.lazypizza.product.domain.model.CartTopping
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseCartRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val authManager: AuthManager = AuthManager()
) : CartRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val CART_COLLECTION = "cart"
    }

    override fun getCartItems(): Flow<List<CartItem>> {
        return authManager.observeAuthState()
            .flatMapLatest { user ->
                if (user == null) {
                    Timber.d("No user, returning empty cart")
                    flowOf(emptyList())
                } else {
                    Timber.d("Observing cart for user: ${user.uid}")
                    observeCartForUser(user.uid)
                }
            }
    }

    private fun observeCartForUser(userId: String): Flow<List<CartItem>> = callbackFlow {
        val listener = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(CART_COLLECTION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Error observing cart for user $userId")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        @Suppress("UNCHECKED_CAST")
                        val toppingsData = doc.get("toppings") as? List<Map<String, Any>> ?: emptyList()
                        val toppings = toppingsData.mapNotNull { toppingMap ->
                            try {
                                CartTopping(
                                    id = toppingMap["id"] as? String ?: "",
                                    name = toppingMap["name"] as? String ?: "",
                                    price = (toppingMap["price"] as? Number)?.toDouble() ?: 0.0,
                                    quantity = (toppingMap["quantity"] as? Number)?.toInt() ?: 1
                                )
                            } catch (e: Exception) {
                                Timber.e(e, "Error parsing topping")
                                null
                            }
                        }

                        CartItem(
                            id = doc.id,
                            productId = doc.getString("productId") ?: "",
                            name = doc.getString("name") ?: "",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            basePrice = doc.getDouble("basePrice") ?: 0.0,
                            quantity = doc.getLong("quantity")?.toInt() ?: 1,
                            toppings = toppings,
                            category = doc.getString("category") ?: "",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing cart item")
                        null
                    }
                } ?: emptyList()

                Timber.d("Cart updated: ${items.size} items, total quantity: ${items.sumOf { it.quantity }}")
                trySend(items)
            }

        awaitClose {
            Timber.d("Closing cart observer for user $userId")
            listener.remove()
        }
    }

    override fun getCartSummary(): Flow<CartSummary> {
        return getCartItems().map { items ->
            CartSummary(items = items)
        }
    }

    override fun getCartItemsCount(): Flow<Int> {
        return getCartItems().map { items ->
            val count = items.sumOf { it.quantity }
            Timber.d("Cart count: $count")
            count
        }
    }

    override suspend fun transferCart(
        fromUserId: String,
        toUserId: String
    ): Result<Unit, DataError.Network> {
        return try {
            Timber.d("Transferring cart from $fromUserId to $toUserId")

            val guestCartSnapshot = firestore
                .collection(USERS_COLLECTION)
                .document(fromUserId)
                .collection(CART_COLLECTION)
                .get()
                .await()

            if (guestCartSnapshot.isEmpty) {
                Timber.d("Guest cart is empty, nothing to transfer")
                return Result.Success(Unit)
            }

            // Copy to authenticated user's cart
            val batch = firestore.batch()
            var itemsTransferred = 0

            guestCartSnapshot.documents.forEach { doc ->
                val data = doc.data
                if (data != null) {
                    val newDocRef = firestore
                        .collection(USERS_COLLECTION)
                        .document(toUserId)
                        .collection(CART_COLLECTION)
                        .document(doc.id)

                    batch.set(newDocRef, data)
                    itemsTransferred++
                }
            }

            // Delete guest cart items
            guestCartSnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            batch.commit().await()
            Timber.d("Successfully transferred $itemsTransferred items")
            Result.Success(Unit)

        } catch (e: Exception) {
            Timber.e(e, "Failed to transfer cart from $fromUserId to $toUserId")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun addToCart(item: CartItem): EmptyResult<DataError> {
        val cartCollection = getCartCollection()
            ?: return Result.Error(DataError.Network.UNKNOWN)

        return try {
            val itemData = hashMapOf(
                "id" to item.id,
                "productId" to item.productId,
                "name" to item.name,
                "imageUrl" to item.imageUrl,
                "basePrice" to item.basePrice,
                "quantity" to item.quantity,
                "category" to item.category,
                "timestamp" to item.timestamp,
                "toppings" to item.toppings.map { topping ->
                    hashMapOf(
                        "id" to topping.id,
                        "name" to topping.name,
                        "price" to topping.price,
                        "quantity" to topping.quantity
                    )
                }
            )

            val existingDoc = cartCollection.document(item.id).get().await()

            if (existingDoc.exists()) {
                val existingQuantity = existingDoc.getLong("quantity")?.toInt() ?: 0
                val newQuantity = existingQuantity + item.quantity

                cartCollection.document(item.id)
                    .update("quantity", newQuantity)
                    .await()
            } else {
                cartCollection.document(item.id)
                    .set(itemData)
                    .await()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error adding to cart")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateQuantity(
        itemId: String,
        quantity: Int
    ): EmptyResult<DataError> {
        if (quantity <= 0) {
            return removeFromCart(itemId)
        }

        val cartCollection = getCartCollection()
            ?: return Result.Error(DataError.Network.UNKNOWN)

        return try {
            cartCollection.document(itemId)
                .update("quantity", quantity)
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating quantity")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun removeFromCart(itemId: String): EmptyResult<DataError> {
        val cartCollection = getCartCollection()
            ?: return Result.Error(DataError.Network.UNKNOWN)
        return try {
            cartCollection
                .document(itemId)
                .delete()
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error removing from cart")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun clearCart(): EmptyResult<DataError> {
        val cartCollection = getCartCollection()
            ?: return Result.Error(DataError.Network.UNKNOWN)
        return try {
            val snapshot = cartCollection.get().await()
            firestore.runBatch { batch ->
                snapshot?.documents?.forEach { doc ->
                    batch.delete(doc.reference)
                }
            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing cart")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    private suspend fun getCartCollection(): CollectionReference? {
        return when (val result = authManager.ensureAuthenticated()) {
            is Result.Success -> {
                firestore.collection(USERS_COLLECTION)
                    .document(result.data)
                    .collection(CART_COLLECTION)
            }
            is Result.Error -> null
        }
    }
}