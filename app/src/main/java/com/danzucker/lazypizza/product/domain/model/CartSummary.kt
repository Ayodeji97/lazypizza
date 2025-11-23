package com.danzucker.lazypizza.product.domain.model

/**
 * Domain model for cart summary
 * Maps to Firebase Firestore:
 * carts/{userId}/summary
 */
data class CartSummary(
    val items: List<CartItem>,
    val totalItems: Int = items.sumOf { it.quantity },
    val subtotal: Double = items.sumOf { it.totalPrice },
    val tax: Double = subtotal * 0.13, // 13% tax
    val deliveryFee: Double = 5.00,
    val total: Double = subtotal + tax + deliveryFee
)