package com.danzucker.lazypizza.product.presentation.models

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
    val cardType: LazyPizzaCardType,
    val quantityInCart: Int = 0
)

enum class LazyPizzaCardType {
    PIZZA, OTHERS
}