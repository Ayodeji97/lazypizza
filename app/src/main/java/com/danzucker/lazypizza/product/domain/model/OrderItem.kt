package com.danzucker.lazypizza.product.domain.model

/**
 * Domain model for an order item
 */
data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val imageUrl: String = "",
    val toppings: List<OrderTopping> = emptyList(),
    val category: String = "",
) {
    fun toFirestoreMap(): Map<String, Any> =
        mapOf(
            "productId" to productId,
            "productName" to productName,
            "quantity" to quantity,
            "unitPrice" to unitPrice,
            "totalPrice" to totalPrice,
            "imageUrl" to imageUrl,
            "toppings" to toppings.map { it.toFirestoreMap() },
            "category" to category,
        )

    companion object {
        fun fromFirestoreMap(data: Map<String, Any>): OrderItem {
            @Suppress("UNCHECKED_CAST")
            val toppingsData = data["toppings"] as? List<Map<String, Any>> ?: emptyList()

            return OrderItem(
                productId = data["productId"] as? String ?: "",
                productName = data["productName"] as? String ?: "",
                quantity = (data["quantity"] as? Number)?.toInt() ?: 0,
                unitPrice = (data["unitPrice"] as? Number)?.toDouble() ?: 0.0,
                totalPrice = (data["totalPrice"] as? Number)?.toDouble() ?: 0.0,
                imageUrl = data["imageUrl"] as? String ?: "",
                toppings = toppingsData.map { OrderTopping.fromFirestoreMap(it) },
                category = data["category"] as? String ?: "",
            )
        }
    }
}

/**
 * Domain model for an order topping
 */
data class OrderTopping(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
) {
    fun toFirestoreMap(): Map<String, Any> =
        mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "quantity" to quantity,
        )

    companion object {
        fun fromFirestoreMap(data: Map<String, Any>): OrderTopping =
            OrderTopping(
                id = data["id"] as? String ?: "",
                name = data["name"] as? String ?: "",
                price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                quantity = (data["quantity"] as? Number)?.toInt() ?: 0,
            )
    }
}

enum class OrderStatus {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
