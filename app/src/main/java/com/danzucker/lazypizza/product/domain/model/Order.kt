package com.danzucker.lazypizza.product.domain.model

data class Order(
    val id: String,
    val orderNumber: String,
    val date: Long, // timestamp
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val userId: String
)

data class OrderItem(
    val productName: String,
    val quantity: Int,
    val price: Double
)

enum class OrderStatus {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;
}
