package com.danzucker.lazypizza.product.presentation.models

import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaCardType
data class LazyPizzaListUi(
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