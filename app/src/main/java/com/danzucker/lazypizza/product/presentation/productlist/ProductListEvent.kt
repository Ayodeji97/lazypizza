package com.danzucker.lazypizza.product.presentation.productlist

sealed interface ProductListEvent {
    data class OpenPhoneDialer(val phoneNumber: String) : ProductListEvent
    data object ItemAddedToCart : ProductListEvent
    data object FailedToAddToCart : ProductListEvent
}