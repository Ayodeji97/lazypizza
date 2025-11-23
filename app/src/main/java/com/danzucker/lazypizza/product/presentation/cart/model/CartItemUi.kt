package com.danzucker.lazypizza.product.presentation.cart.model

data class CartItemUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double,
    val toppings: Map<String, Int> = emptyMap() // topping name to quantity
)