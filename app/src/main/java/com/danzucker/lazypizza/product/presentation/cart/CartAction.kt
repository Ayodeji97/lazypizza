package com.danzucker.lazypizza.product.presentation.cart

sealed interface CartAction {
    data object BackToMenu : CartAction
    data object OnProceedToCheckout : CartAction
    data class OnQuantityChange(val itemId: String, val quantity: Int) : CartAction
    data class OnDeleteItem(val itemId: String) : CartAction
    data class OnAddRecommendedItem(val itemId: String) : CartAction
}