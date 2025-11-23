package com.danzucker.lazypizza.product.domain.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: ProductCategory = ProductCategory.PIZZA,
    val isAvailable: Boolean = true,
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val type: ProductType = ProductType.PIZZA
)

enum class ProductCategory(val displayName: String) {
    PIZZA("Pizza"),
    DRINKS("Drinks"),
    SAUCES("Sauces"),
    ICE_CREAM("Ice Cream");

    companion object {
        fun fromString(value: String): ProductCategory {
            return entries.find { it.displayName.equals(value, ignoreCase = true) } ?: PIZZA
        }
    }
}

enum class ProductType {
    PIZZA,
    OTHER
}
