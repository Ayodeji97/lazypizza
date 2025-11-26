package com.danzucker.lazypizza.product.data

import com.google.firebase.firestore.FirebaseFirestore
import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.product.ProductRepository
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.domain.model.ProductType
import com.danzucker.lazypizza.product.domain.model.Topping
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseProductRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ProductRepository {

    companion object {
        private const val PRODUCTS_COLLECTION = "products"
        private const val TOPPINGS_COLLECTION = "toppings"
    }

    /**
     * Get all products with real-time updates
     */
    override fun getProducts(): Flow<Result<List<Product>, DataError.Network>> = callbackFlow {
        val listener = firestore.collection(PRODUCTS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Product(
                            id = doc.getString("id") ?: "",
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            imageUrl = doc.getString("imageUrl") ?: "",
                            category = ProductCategory.fromString(
                                doc.getString("category") ?: ""
                            ),
                            isAvailable = doc.getBoolean("isAvailable") ?: true,
                            rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                            reviewsCount = doc.getLong("reviewsCount")?.toInt() ?: 0,
                            type = ProductType.valueOf(
                                doc.getString("type") ?: "OTHER"
                            )
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Result.Success(products))
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get product by ID (one-time fetch)
     */
    override suspend fun getProductById(id: String): Result<Product?, DataError.Network> {
        return try {
            val doc = firestore.collection(PRODUCTS_COLLECTION)
                .document(id)
                .get()
                .await()

            if (!doc.exists()) {
                return Result.Success(null)
            }

            val product = Product(
                id = doc.getString("id") ?: "",
                name = doc.getString("name") ?: "",
                description = doc.getString("description") ?: "",
                price = doc.getDouble("price") ?: 0.0,
                imageUrl = doc.getString("imageUrl") ?: "",
                category = ProductCategory.fromString(
                    doc.getString("category") ?: ""
                ),
                isAvailable = doc.getBoolean("isAvailable") ?: true,
                rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                reviewsCount = doc.getLong("reviewsCount")?.toInt() ?: 0,
                type = ProductType.valueOf(
                    doc.getString("type") ?: "OTHER"
                )
            )

            Result.Success(product)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    /**
     * Get all toppings with real-time updates
     */
    override fun getToppings(): Flow<Result<List<Topping>, DataError.Network>> = callbackFlow {
        val listener = firestore.collection(TOPPINGS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                    return@addSnapshotListener
                }

                val toppings = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Topping(
                            id = doc.getString("id") ?: "",
                            name = doc.getString("name") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            imageUrl = doc.getString("imageUrl") ?: "",
                            maxQuantity = doc.getLong("maxQuantity")?.toInt() ?: 3,
                            isAvailable = doc.getBoolean("isAvailable") ?: true
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Result.Success(toppings))
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get products by category
     */
    override fun getProductsByCategory(category: ProductCategory): Flow<Result<List<Product>, DataError.Network>> = callbackFlow {
        val listener = firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo("category", category.displayName)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(DataError.Network.UNKNOWN))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Product(
                            id = doc.getString("id") ?: "",
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            imageUrl = doc.getString("imageUrl") ?: "",
                            category = ProductCategory.fromString(
                                doc.getString("category") ?: ""
                            ),
                            isAvailable = doc.getBoolean("isAvailable") ?: true,
                            rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                            reviewsCount = doc.getLong("reviewsCount")?.toInt() ?: 0,
                            type = ProductType.valueOf(
                                doc.getString("type") ?: "OTHER"
                            )
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Result.Success(products))
            }

        awaitClose { listener.remove() }
    }
}