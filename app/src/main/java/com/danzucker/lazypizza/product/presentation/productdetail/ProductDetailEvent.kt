package com.danzucker.lazypizza.product.presentation.productdetail

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface ProductDetailEvent {
    data object NavigateBack : ProductDetailEvent
    data object NavigateBackToMenu : ProductDetailEvent

    data class ShowMessage(val message: UiText) : ProductDetailEvent
    data class ShowErrorMessage(val message: UiText) : ProductDetailEvent
}