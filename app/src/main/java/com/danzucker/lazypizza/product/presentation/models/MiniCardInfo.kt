package com.danzucker.lazypizza.product.presentation.models

data class MiniCardInfo(
    val id: String,
    val title: String,
    val price: String,
    val imageUrl: String,
    val quantity: Int = 0,
    val isSelected: Boolean = false
)