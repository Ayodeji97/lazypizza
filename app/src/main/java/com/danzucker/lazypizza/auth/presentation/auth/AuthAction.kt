package com.danzucker.lazypizza.auth.presentation.auth

sealed interface AuthAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : AuthAction
    data class OnCodeChange(val index: Int, val digit: String) : AuthAction
    data class OnCodeBoxFocused(val index: Int) : AuthAction
    data object OnContinueClick : AuthAction
    data object OnContinueWithoutSignIn : AuthAction
    data object OnResendCodeClick : AuthAction
    data object OnBackPressed : AuthAction
}