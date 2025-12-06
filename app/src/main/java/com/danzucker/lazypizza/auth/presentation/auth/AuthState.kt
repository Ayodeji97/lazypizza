package com.danzucker.lazypizza.auth.presentation.auth

import com.danzucker.lazypizza.core.presentation.util.UiText

data class AuthState(
    val phoneNumber: String = "",
    val verificationCode: List<String> = List(6) { "" },
    val focusedBoxIndex: Int = 0,
    val currentStep: AuthStep = AuthStep.OTP_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val resendCountdown: Int = 60,
    val canLogin: Boolean = false,
    val canResend: Boolean = false,
    val verificationId: String? = null
)

enum class AuthStep {
    PHONE_INPUT,
    OTP_INPUT
}