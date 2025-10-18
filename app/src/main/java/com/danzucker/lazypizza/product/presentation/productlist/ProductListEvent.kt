package com.danzucker.lazypizza.product.presentation.productlist

sealed interface ProductListEvent {
    data class OpenPhoneDialer(val phoneNumber: String) : ProductListEvent
}