package com.danzucker.lazypizza.product.presentation.models

data class ToppingUi(
    val id: String,
    val name: String,
    val price: String,
    val imageUrl: String,
    val quantity: Int = 0,
    val maxQuantity: Int = 3
)