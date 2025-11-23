package com.danzucker.lazypizza.product.domain.model

import com.danzucker.lazypizza.product.domain.model.CartTopping

/**
 * Domain model for a cart item
 * Maps to Firebase Firestore structure:
 * carts/{userId}/items/{cartItemId}
 */
data class CartItem(
    val id: String, // Unique cart item ID (product ID + toppings hash for pizzas)
    val productId: String, // Reference to the product
    val name: String,
    val imageUrl: String,
    val basePrice: Double,
    val quantity: Int,
    val toppings: List<CartTopping> = emptyList(), // Empty for non-pizza items
    val category: String, // "Pizza", "Drinks", "Sauces", "Ice Cream"
    val timestamp: Long = System.currentTimeMillis() // For ordering items
) {
    /**
     * Calculate total price including toppings
     */
    val totalPrice: Double
        get() {
            val toppingsTotal = toppings.sumOf { it.price * it.quantity }
            return (basePrice + toppingsTotal) * quantity
        }

    /**
     * Get unit price (base + toppings for one item)
     */
    val unitPrice: Double
        get() = basePrice + toppings.sumOf { it.price * it.quantity }

    /**
     * Firebase Firestore representation
     */
    fun toFirestoreMap(): Map<String, Any> = mapOf(
        "id" to id,
        "productId" to productId,
        "name" to name,
        "imageUrl" to imageUrl,
        "basePrice" to basePrice,
        "quantity" to quantity,
        "toppings" to toppings.map { it.toFirestoreMap() },
        "category" to category,
        "timestamp" to timestamp
    )

    companion object {
        /**
         * Create from Firebase Firestore data
         */
        fun fromFirestoreMap(data: Map<String, Any>): CartItem {
            return CartItem(
                id = data["id"] as? String ?: "",
                productId = data["productId"] as? String ?: "",
                name = data["name"] as? String ?: "",
                imageUrl = data["imageUrl"] as? String ?: "",
                basePrice = (data["basePrice"] as? Number)?.toDouble() ?: 0.0,
                quantity = (data["quantity"] as? Number)?.toInt() ?: 0,
                toppings = (data["toppings"] as? List<Map<String, Any>>)?.map {
                    CartTopping.Companion.fromFirestoreMap(it)
                } ?: emptyList(),
                category = data["category"] as? String ?: "",
                timestamp = (data["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }

        /**
         * Generate unique cart item ID based on product and toppings
         */
        fun generateId(productId: String, toppings: List<CartTopping>): String {
            if (toppings.isEmpty()) {
                return productId
            }
            // For pizzas with toppings, create unique ID
            val toppingIds = toppings.sortedBy { it.id }.joinToString("-") {
                "${it.id}x${it.quantity}"
            }
            return "${productId}_$toppingIds"
        }
    }
}