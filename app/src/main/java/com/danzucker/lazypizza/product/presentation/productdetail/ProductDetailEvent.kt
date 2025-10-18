package com.danzucker.lazypizza.product.presentation.productdetail

sealed interface ProductDetailEvent {
    data object NavigateBack : ProductDetailEvent
}