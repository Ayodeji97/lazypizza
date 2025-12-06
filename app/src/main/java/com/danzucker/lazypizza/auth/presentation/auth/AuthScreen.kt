package com.danzucker.lazypizza.auth.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.ObserveAsEvents
import com.danzucker.lazypizza.core.presentation.util.applyIf
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthRoot(
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AuthEvent.MoveFocusToBox -> {}
            is AuthEvent.NavigateBack -> {}
            is AuthEvent.NavigateToHome -> {}
            is AuthEvent.ShowErrorMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is AuthEvent.ShowMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    AuthScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)
    val isMobilePortrait = deviceScreenType == MOBILE_PORTRAIT
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        when (state.currentStep) {
            AuthStep.PHONE_INPUT -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthPhoneInputSection(
                        state = state,
                        onAction = onAction,
                        isMobilePortrait = isMobilePortrait,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }

            AuthStep.OTP_INPUT -> {
                AuthPhoneOtpSection(
                    state = state,
                    onAction = onAction,
                    isMobilePortrait = deviceScreenType == MOBILE_PORTRAIT,
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Preview(
    name = "AuthScreen",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AuthScreenPreview() {
    LazyPizzaTheme {
        AuthScreen(
            state = AuthState(),
            onAction = {}
        )
    }
}