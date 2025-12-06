package com.danzucker.lazypizza.auth.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimaryButton
import com.danzucker.lazypizza.core.presentation.designsystem.textfield.LazyPizzaTextField
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun AuthPhoneInputContent(
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
            text = stringResource(R.string.welcome_subtitle),
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
    }
}

@Preview
@Composable
private fun AuthPhoneInputContentPreview() {
    LazyPizzaTheme {
        AuthPhoneInputContent(
            state = AuthState(
                canLogin = false
            ),
            onAction = {},
            modifier = Modifier
        )
    }
}