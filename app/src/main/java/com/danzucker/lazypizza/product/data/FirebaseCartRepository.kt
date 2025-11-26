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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * Firebase implementation of CartRepository
 * Stores user's cart in Firestore: users/{userId}/cart/{cartItemId}
 */
class FirebaseCartRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val authManager: AuthManager = AuthManager()
) : CartRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val CART_COLLECTION = "cart"
    }

    override fun getCartItems(): Flow<List<CartItem>> = callbackFlow {
        val userId = when (val result = authManager.ensureAuthenticated()) {
            is Result.Success -> result.data
            is Result.Error -> {
                trySend(emptyList())
                close()
                return@callbackFlow
            }
        }

        val listener = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(CART_COLLECTION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
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
                        null
                    }
                } ?: emptyList()
                trySend(items)
            }

        awaitClose {
            listener.remove()
        }
    }

    override fun getCartSummary(): Flow<CartSummary> {
        return getCartItems().map { items ->
            CartSummary(items = items)
        }
    }

    override suspend fun addToCart(item: CartItem): EmptyResult<DataError> {
        val cartCollection = getCartCollection()
            ?: return Result.Error(DataError.Network.UNKNOWN)

        return try {
            // Convert CartItem to Firestore document
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

            // Check if item already exists
            val existingDoc = cartCollection.document(item.id).get().await()

            if (existingDoc.exists()) {
                // Update quantity if item exists
                val existingQuantity = existingDoc.getLong("quantity")?.toInt() ?: 0
                val newQuantity = existingQuantity + item.quantity

                cartCollection.document(item.id)
                    .update("quantity", newQuantity)
                    .await()
            } else {
                // Add new item
                cartCollection.document(item.id)
                    .set(itemData)
                    .await()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
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
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override fun getCartItemsCount(): Flow<Int> {
        return getCartItems().map { items ->
            items.sumOf { it.quantity }
        }
    }

    /**
     * Get reference to user's cart collection
     * Returns null if authentication fails
     */
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