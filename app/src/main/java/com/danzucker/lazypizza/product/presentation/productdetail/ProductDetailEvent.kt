package com.danzucker.lazypizza.product.presentation.productdetail

import com.danzucker.lazypizza.product.presentation.productlist.ProductListEvent

sealed interface ProductDetailEvent {
    data object NavigateBack : ProductDetailEvent
    data object NavigateBackToMenu : ProductDetailEvent
    data object FailedToAddToCart : ProductDetailEvent
}