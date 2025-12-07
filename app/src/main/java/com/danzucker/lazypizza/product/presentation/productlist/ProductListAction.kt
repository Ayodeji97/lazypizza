package com.danzucker.lazypizza.product.presentation.productlist

sealed interface ProductListAction {
    data class OnSearchQueryChange(val query: String) : ProductListAction
    data class OnCategorySelected(val category: String) : ProductListAction
    data class OnProductClick(val productId: String) : ProductListAction
    data class OnAddToCart(val productId: String) : ProductListAction
    data class OnQuantityChange(val productId: String, val quantity: Int) : ProductListAction
    data class OnDeleteFromCart(val productId: String) : ProductListAction
    data object OnPhoneNumberClick : ProductListAction
    data object OnScrollToCategoryComplete : ProductListAction
    data object OnUserIconClick : ProductListAction
    data object OnLogoutConfirmed : ProductListAction
    data object OnLogoutCancelled : ProductListAction
}