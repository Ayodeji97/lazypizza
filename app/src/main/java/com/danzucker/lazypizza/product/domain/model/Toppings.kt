package com.danzucker.lazypizza.product.domain.model

data class Topping(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val maxQuantity: Int = 3,
    val isAvailable: Boolean = true
)
