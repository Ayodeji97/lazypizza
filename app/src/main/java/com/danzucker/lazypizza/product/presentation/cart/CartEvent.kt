package com.danzucker.lazypizza.product.presentation.cart

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface CartEvent {
    data object NavigateBack : CartEvent
    data class ShowErrorMessage(val message: UiText) : CartEvent
    data class ShowMessage(val message: UiText) : CartEvent
}