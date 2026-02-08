package com.danzucker.lazypizza.product.presentation.checkout

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface CheckoutEvent {
    data object NavigateBack : CheckoutEvent
    data object ShowDatePicker : CheckoutEvent
    data object ShowTimePicker : CheckoutEvent
    data object NavigateToOrderConfirmation : CheckoutEvent
    data class ShowError(val message: UiText) : CheckoutEvent
    data class ShowMessage(val message: UiText) : CheckoutEvent
}