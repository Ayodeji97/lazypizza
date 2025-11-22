package com.danzucker.lazypizza.product.presentation.cart

import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi

data class CartState(
    val cartItems: List<CartItemUi> = emptyList(),
    val recommendedAddOns: List<RecommendedAddOnUi> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoadingData: Boolean = false
) {
    val hasProducts: Boolean = cartItems.isNotEmpty()
}