package com.danzucker.lazypizza.auth.presentation.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.auth.presentation.auth.components.OtpCodeBox
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimaryButton
import com.danzucker.lazypizza.core.presentation.designsystem.textfield.LazyPizzaTextField
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.util.formatTime

@Composable
fun AuthPhoneOtpContent(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.enter_code_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surfaceTint
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyPizzaTextField(
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = {
                onAction(AuthAction.OnPhoneNumberChange(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            state.verificationCode.forEachIndexed { index, digit ->
                OtpCodeBox(
                    digit = digit,
                    isError = state.errorMessage != null,
                    onCodeChange = {

                    },
                    onFocused = {

                    },
                    imeAction = if (index == 5) ImeAction.Done else ImeAction.Next,
                    onImeAction = {
                        if (index < 5 && digit.isNotEmpty()) {

                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
            }
        }

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.errorMessage.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = stringResource(R.string.continue_btn),
            onClick = { onAction(AuthAction.OnContinueClick) },
            enabled = state.canLogin,
            isLoading = state.isLoading
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = stringResource(R.string.continue_without_signing),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(11.dp))

        if (state.canResend) {
            TextButton(
                onClick = {
                    onAction(AuthAction.OnResendCodeClick )
                }
            ) {
                Text(
                    stringResource(R.string.code_resend),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            val formattedTime = formatTime(
                state.resendCountdown / 60,
                state.resendCountdown % 60
            )
            Text(
                text = stringResource(R.string.code_request_message, formattedTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }
    }
}

@Preview(
    name = "AuthPhoneOtpContent",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AuthPhoneInputContentPreview() {
    LazyPizzaTheme {
        AuthPhoneOtpContent(
            state = AuthState(
                canLogin = false
            ),
            onAction = {},
            modifier = Modifier
        )
    }
}