package com.danzucker.lazypizza.product.domain.model

import kotlin.collections.map


/**
 * Domain model for an order
 *
 * Firebase structure:
 * /orders/{orderId} - Main orders collection
 * /users/{userId}/orders/{orderId} - User-specific orders for quick lookup
 */
data class Order(
    val id: String = "",
    val orderNumber: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val pickupTime: String = "", // Formatted pickup time (e.g., "SEPTEMBER 25, 12:15" or "12:15")
    val pickupTimeMillis: Long = 0L, // Unix timestamp for sorting/filtering
    val comment: String = "",
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,
    val status: OrderStatus = OrderStatus.IN_PROGRESS,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Firebase Firestore representation
     */
    fun toFirestoreMap(): Map<String, Any> = mapOf(
        "id" to id,
        "orderNumber" to orderNumber,
        "userId" to userId,
        "items" to items.map { it.toFirestoreMap() },
        "pickupTime" to pickupTime,
        "pickupTimeMillis" to pickupTimeMillis,
        "comment" to comment,
        "subtotal" to subtotal,
        "tax" to tax,
        "total" to total,
        "status" to status.name,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )

    companion object {
        /**
         * Create from Firebase Firestore data
         */
        fun fromFirestoreMap(data: Map<String, Any>): Order {
            @Suppress("UNCHECKED_CAST")
            val itemsData = data["items"] as? List<Map<String, Any>> ?: emptyList()

            val statusString = data["status"] as? String
            val status = enumValues<OrderStatus>().firstOrNull { it.name == statusString }
                ?: OrderStatus.IN_PROGRESS

            return Order(
                id = data["id"] as? String ?: "",
                orderNumber = data["orderNumber"] as? String ?: "",
                userId = data["userId"] as? String ?: "",
                items = itemsData.map { OrderItem.fromFirestoreMap(it) },
                pickupTime = data["pickupTime"] as? String ?: "",
                pickupTimeMillis = (data["pickupTimeMillis"] as? Number)?.toLong() ?: 0L,
                comment = data["comment"] as? String ?: "",
                subtotal = (data["subtotal"] as? Number)?.toDouble() ?: 0.0,
                tax = (data["tax"] as? Number)?.toDouble() ?: 0.0,
                total = (data["total"] as? Number)?.toDouble() ?: 0.0,
                status = status,
                createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                updatedAt = (data["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }

        /**
         * Generate order number from timestamp
         * Format: Order #{last 5 digits of timestamp}
         * Example: Order #12345
         */
        fun generateOrderNumber(timestamp: Long = System.currentTimeMillis()): String {
            val last5Digits = timestamp.toString().takeLast(5)
            return "#$last5Digits"
        }
    }
}
