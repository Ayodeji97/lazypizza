package com.danzucker.lazypizza.auth.presentation.auth

import com.danzucker.lazypizza.core.presentation.util.UiText

sealed interface AuthEvent {
    data object NavigateToHome : AuthEvent
    data object NavigateBack : AuthEvent
    data class ShowMessage(val message: UiText) : AuthEvent
    data class ShowErrorMessage(val message: UiText) : AuthEvent
}