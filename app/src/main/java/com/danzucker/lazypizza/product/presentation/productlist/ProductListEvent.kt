package com.danzucker.lazypizza.product.presentation.productlist

import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailEvent

sealed interface ProductListEvent {
    data class OpenPhoneDialer(val phoneNumber: String) : ProductListEvent
    data object ItemAddedToCart : ProductListEvent
    data class ShowErrorMessage(val message: UiText) : ProductListEvent
}