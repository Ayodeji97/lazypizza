package com.danzucker.lazypizza.product.domain.model

/**
 * Domain model for cart topping
 */

data class CartTopping(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
) {
    fun toFirestoreMap(): Map<String, Any> = mapOf(
        "id" to id,
        "name" to name,
        "price" to price,
        "quantity" to quantity
    )

    companion object {
        fun fromFirestoreMap(data: Map<String, Any>): CartTopping {
            return CartTopping(
                id = data["id"] as? String ?: "",
                name = data["name"] as? String ?: "",
                price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                quantity = (data["quantity"] as? Number)?.toInt() ?: 0
            )
        }
    }
}