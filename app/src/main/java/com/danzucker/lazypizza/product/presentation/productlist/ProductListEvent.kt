package com.danzucker.lazypizza.product.presentation.productlist

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface ProductListEvent {
    data class OpenPhoneDialer(val phoneNumber: String) : ProductListEvent
    data object ItemAddedToCart : ProductListEvent
    data class ShowErrorMessage(val message: UiText) : ProductListEvent
}