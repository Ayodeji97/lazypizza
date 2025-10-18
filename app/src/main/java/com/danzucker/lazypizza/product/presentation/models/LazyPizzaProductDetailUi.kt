package com.danzucker.lazypizza.product.presentation.models

data class PizzaDetailUi(
    val id: String,
    val name: String,
    val description: String,
    val basePrice: Double,
    val imageUrl: String,
    val imageResId: Int, // For local resources - fall back option
    val ingredients: String,
    val category: String
)