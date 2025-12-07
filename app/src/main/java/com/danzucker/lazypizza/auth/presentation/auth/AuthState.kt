package com.danzucker.lazypizza.auth.presentation.auth

import com.danzucker.lazypizza.core.presentation.util.UiText

data class AuthState(
    val phoneNumber: String = "",
    val verificationCode: List<String> = List(6) { "" },
    val focusedBoxIndex: Int = 0,
    val currentStep: AuthStep = AuthStep.PHONE_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val resendCountdown: Int = 60,
    val canResend: Boolean = false,
    val verificationId: String? = null
) {
    val canLogin: Boolean
        get() = when (currentStep) {
            AuthStep.PHONE_INPUT -> phoneNumber.isNotEmpty() && !isLoading
            AuthStep.OTP_INPUT -> verificationCode.all { it.isNotEmpty() } && !isLoading
        }

    val isCodeComplete: Boolean
        get() = verificationCode.all { it.isNotEmpty() }
}

enum class AuthStep {
    PHONE_INPUT,
    OTP_INPUT
}