package com.danzucker.lazypizza.product.presentation.productdetail

sealed interface ProductDetailAction {
    data object OnBackClick : ProductDetailAction
    data class OnToppingClick(val toppingId: String) : ProductDetailAction
    data class OnToppingQuantityChange(val toppingId: String, val quantity: Int) : ProductDetailAction
    data object OnAddToCartClick : ProductDetailAction
}