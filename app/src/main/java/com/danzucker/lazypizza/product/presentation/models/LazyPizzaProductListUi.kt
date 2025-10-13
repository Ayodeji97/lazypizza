package com.danzucker.lazypizza.product.presentation.models

import com.danzucker.lazypizza.product.presentation.components.LazyPizzaCardType
data class LazyPizzaProductListUi(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val category: String,
    val rating: Float,
    val reviewsCount: Int,
    val isFavorite: Boolean,
    val cardType: LazyPizzaCardType = LazyPizzaCardType.PIZZA
)