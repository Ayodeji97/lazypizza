package com.danzucker.lazypizza.product.presentation.productdetail

data class ProductDetailState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)