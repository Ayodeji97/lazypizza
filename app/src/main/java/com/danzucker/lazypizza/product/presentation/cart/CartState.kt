package com.danzucker.lazypizza.product.presentation.cart

data class CartState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)