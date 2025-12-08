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
import kotlinx.coroutines.flow.distinctUntilChanged
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
            .distinctUntilChanged { old, new -> old?.uid == new?.uid }
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

    /**
     * Transfers cart items from guest (anonymous) user to authenticated user.
     * Should be called after successful phone authentication to preserve cart items.
     *
     * @param fromUserId The source user ID (anonymous/guest user)
     * @param toUserId The destination user ID (authenticated user)
     * @return Success if transfer completed, or Error if transfer failed
     */
    override suspend fun transferCart(
        fromUserId: String,
        toUserId: String
    ): Result<Unit, DataError.Network> {
        return try {
            Timber.d("Transferring cart from $fromUserId to $toUserId")

            // Get guest cart items
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

            // Get existing authenticated user's cart items
            val authenticatedCartSnapshot = firestore
                .collection(USERS_COLLECTION)
                .document(toUserId)
                .collection(CART_COLLECTION)
                .get()
                .await()

            val existingItems = authenticatedCartSnapshot.documents.associateBy { it.id }

            // Copy/merge items to authenticated cart
            val copyBatch = firestore.batch()
            var itemsTransferred = 0
            var itemsMerged = 0

            guestCartSnapshot.documents.forEach { guestDoc ->
                val guestData = guestDoc.data
                if (guestData != null) {
                    val itemRef = firestore
                        .collection(USERS_COLLECTION)
                        .document(toUserId)
                        .collection(CART_COLLECTION)
                        .document(guestDoc.id)

                    val existingItem = existingItems[guestDoc.id]

                    if (existingItem != null) {
                        // Item exists - merge quantities
                        val guestQuantity = (guestData["quantity"] as? Number)?.toInt() ?: 1
                        val existingQuantity = existingItem.getLong("quantity")?.toInt() ?: 1
                        val mergedQuantity = guestQuantity + existingQuantity

                        Timber.d("Merging item ${guestDoc.id}: $guestQuantity + $existingQuantity = $mergedQuantity")
                        copyBatch.update(itemRef, mapOf(
                            "quantity" to mergedQuantity,
                            "timestamp" to System.currentTimeMillis()
                        ))
                        itemsMerged++
                    } else {
                        // New item - add to cart
                        Timber.d("Adding new item ${guestDoc.id}")
                        copyBatch.set(itemRef, guestData)
                        itemsTransferred++
                    }
                }
            }

            // Commit copy batch FIRST
            Timber.d("Committing copy batch: $itemsTransferred new, $itemsMerged merged")
            copyBatch.commit().await()
            Timber.d("Copy batch committed successfully")

            // Only delete guest cart AFTER successful copy
            val deleteBatch = firestore.batch()
            guestCartSnapshot.documents.forEach { doc ->
                deleteBatch.delete(doc.reference)
            }

            Timber.d("Committing delete batch: ${guestCartSnapshot.documents.size} items")
            deleteBatch.commit().await()
            Timber.d("Delete batch committed successfully")

            Timber.d("Cart transfer complete: $itemsTransferred new items, $itemsMerged merged items")
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