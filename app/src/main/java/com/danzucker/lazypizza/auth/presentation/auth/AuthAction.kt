package com.danzucker.lazypizza.auth.presentation.auth

import android.app.Activity

sealed interface AuthAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : AuthAction
    data class OnCodeChange(val index: Int, val digit: String) : AuthAction
    data class OnCodeBoxFocused(val index: Int) : AuthAction
    data class OnContinueClick(val activity: Activity?) : AuthAction
    data object OnContinueWithoutSignIn : AuthAction
    data class OnResendCodeClick(val activity: Activity?) : AuthAction
    data object OnBackPressed : AuthAction
}