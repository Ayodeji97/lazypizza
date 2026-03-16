package com.danzucker.lazypizza.product.presentation.checkout

sealed interface CheckoutAction {
    data class OnPickupTimeSelected(val option: PickupTimeOption) : CheckoutAction
    data object OnScheduleTimeClick : CheckoutAction
    data object OnToggleOrderDetails : CheckoutAction
    data class OnProductClick(val productId: String) : CheckoutAction
    data class OnQuantityChange(val cartItemId: String, val quantity: Int) : CheckoutAction
    data class OnDeleteItem(val cartItemId: String) : CheckoutAction
    data class OnAddRecommendedItem(val addOnId: String) : CheckoutAction
    data class OnCommentChange(val comment: String) : CheckoutAction
    data object OnPlaceOrder : CheckoutAction
    data object OnBackPressed : CheckoutAction
}