package com.danzucker.lazypizza.auth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class AuthViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<AuthEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AuthState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
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
            is AuthAction.OnBackPressed -> {}
            is AuthAction.OnCodeBoxFocused -> {}
            is AuthAction.OnCodeChange -> {}
            is AuthAction.OnContinueClick -> {}
            is AuthAction.OnContinueWithoutSignIn -> {}
            is AuthAction.OnPhoneNumberChange -> {}
            is AuthAction.OnResendCodeClick -> {}
        }
    }

}