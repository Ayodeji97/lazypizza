package com.danzucker.lazypizza.auth.presentation.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.auth.presentation.auth.util.PhoneValidator
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.core.presentation.util.asUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var countdownJob: Job? = null

    private val eventChannel = Channel<AuthEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AuthState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthState()
        )

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnPhoneNumberChange -> handlePhoneNumberChange(action.phoneNumber)
            is AuthAction.OnCodeChange -> handleCodeChange(action.index, action.digit)
            is AuthAction.OnCodeBoxFocused -> handleCodeBoxFocused(action.index)
            is AuthAction.OnContinueClick -> handleContinueClick(action.activity)
            is AuthAction.OnContinueWithoutSignIn -> handleContinueWithoutSignIn()
            is AuthAction.OnResendCodeClick -> handleResendCode(action.activity)
            is AuthAction.OnBackPressed -> handleBackPressed()
        }
    }

    private fun handlePhoneNumberChange(phoneNumber: String) {
        val formatted = PhoneValidator.formatPhoneNumber(phoneNumber)
        _state.update { it.copy(
            phoneNumber = formatted,
            errorMessage = null
        )}
    }

    private fun handleCodeChange(index: Int, digit: String) {
        val oldCode = _state.value.verificationCode          // value before change
        val newCode = oldCode.toMutableList()
        newCode[index] = digit

        _state.update {
            it.copy(
                verificationCode = newCode,
                errorMessage = null
            )
        }

        when {
            // Typed a digit -> move to NEXT box
            digit.isNotEmpty() && index < 5 -> {
                handleCodeBoxFocused(index + 1)
            }

            // Deleted the digit in this box -> move to PREVIOUS box
            // oldCode[index] was non-empty, now it's empty
            digit.isEmpty() && oldCode[index].isNotEmpty() && index > 0 -> {
                handleCodeBoxFocused(index - 1)
            }
        }
    }

    private fun handleCodeBoxFocused(index: Int) {
        _state.update { it.copy(focusedBoxIndex = index) }
    }

    private fun handleContinueClick(activity: Activity?) {
        when (_state.value.currentStep) {
            AuthStep.PHONE_INPUT -> sendVerificationCode(activity)
            AuthStep.OTP_INPUT -> verifyOtpCode()
        }
    }

    private fun handleContinueWithoutSignIn() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = authRepository.signInAnonymously()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(AuthEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = result.error.asUiText()
                    )}
                    eventChannel.send(AuthEvent.ShowErrorMessage(result.error.asUiText()))
                }
            }
        }
    }

    private fun handleResendCode(activity: Activity?) {
        if (_state.value.canResend) {
            sendVerificationCode(activity)
        }
    }

    private fun handleBackPressed() {
        when (_state.value.currentStep) {
            AuthStep.PHONE_INPUT -> {
                viewModelScope.launch {
                    eventChannel.send(AuthEvent.NavigateBack)
                }
            }
            AuthStep.OTP_INPUT -> {
                countdownJob?.cancel()
                _state.update { it.copy(
                    currentStep = AuthStep.PHONE_INPUT,
                    verificationCode = List(6) { "" },
                    verificationId = null,
                    errorMessage = null,
                    resendCountdown = 60,
                    canResend = false
                )}
            }
        }
    }

    private fun sendVerificationCode(activity: Activity?) {
        val phoneNumber = _state.value.phoneNumber

        // Validate phone number
        if (!PhoneValidator.isValidPhoneNumber(phoneNumber)) {
            _state.update { it.copy(
                errorMessage = UiText.StringResource(R.string.error_invalid_phone_number)
            )}
            viewModelScope.launch {
                eventChannel.send(
                    AuthEvent.ShowErrorMessage(UiText.StringResource(R.string.error_invalid_phone_number))
                )
            }
            return
        }

        if (activity == null) {
            _state.update { it.copy(
                errorMessage = UiText.StringResource(R.string.error_generic)
            )}
            viewModelScope.launch {
                eventChannel.send(
                    AuthEvent.ShowErrorMessage(UiText.StringResource(R.string.error_generic))
                )
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.sendVerificationCode(
                phoneNumber = phoneNumber,
                activity = activity,
                onCodeSent = { verificationId ->
                    _state.update { it.copy(
                        verificationId = verificationId,
                        currentStep = AuthStep.OTP_INPUT,
                        isLoading = false,
                        errorMessage = null,
                        focusedBoxIndex = 0
                    )}
                    startResendCountdown()

                    viewModelScope.launch {
                        eventChannel.send(
                            AuthEvent.ShowMessage(UiText.StringResource(R.string.code_sent_message))
                        )
                    }
                },
                onVerificationCompleted = {
                    // Instant verification (rare, but possible on some devices)
                    _state.update { it.copy(isLoading = false) }
                    viewModelScope.launch {
                        eventChannel.send(AuthEvent.NavigateToHome)
                    }
                },
                onVerificationFailed = { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = error.asUiText()
                    )}
                    viewModelScope.launch {
                        eventChannel.send(AuthEvent.ShowErrorMessage(error.asUiText()))
                    }
                }
            )
        }
    }

    private fun verifyOtpCode() {
        val verificationId = _state.value.verificationId
        val code = _state.value.verificationCode.joinToString("")

        if (verificationId == null) {
            _state.update { it.copy(
                errorMessage = UiText.StringResource(R.string.error_verification_failed)
            )}
            return
        }

        if (code.length != 6) {
            _state.update { it.copy(
                errorMessage = UiText.StringResource(R.string.error_invalid_code)
            )}
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val anonymousUserId = authRepository.getCurrentUserId()
            Timber.d("Captured anonymous user ID before verification: $anonymousUserId")
            when (val result = authRepository.verifyCode(verificationId, code)) {
                is Result.Success -> {
                    Timber.d("Phone verification successful, transferring cart...")
                    // Transfer guest cart if any
                    when (val transferResult = authRepository.transferGuestCart(fromUserId = anonymousUserId)) {
                        is Result.Success -> {
                            Timber.d("Cart transferred successfully")
                        }
                        is Result.Error -> {
                            Timber.e("Cart transfer failed: ${transferResult.error}")

                            eventChannel.send(
                                AuthEvent.ShowMessage(
                                    UiText.StringResource(R.string.cart_transfer_warning)
                                )
                            )
                        }
                    }
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(AuthEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = result.error.asUiText(),
                        verificationCode = List(6) { "" } // Clear code on error
                    )}
                    eventChannel.send(AuthEvent.ShowErrorMessage(result.error.asUiText()))

                    // Move focus back to first box
                    handleCodeBoxFocused(0)
                }
            }
        }
    }

    private fun startResendCountdown() {
        countdownJob?.cancel()
        _state.update { it.copy(
            resendCountdown = 60,
            canResend = false
        )}

        countdownJob = viewModelScope.launch {
            repeat(60) {
                delay(1000)
                val newCountdown = _state.value.resendCountdown - 1
                _state.update { it.copy(resendCountdown = newCountdown) }

                if (newCountdown == 0) {
                    _state.update { it.copy(canResend = true) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}