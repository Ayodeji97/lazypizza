package com.danzucker.lazypizza.auth.presentation.auth

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.applyIf

@Composable
fun AuthPhoneInputSection(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    activity: Activity?,
    isMobilePortrait: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        AuthPhoneInputContent(
            state = state,
            onAction = onAction,
            activity = activity,
            modifier = Modifier
                .applyIf(!isMobilePortrait) {
                    fillMaxWidth(0.5f)
                }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(
    name = "AuthPhoneInputSection",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AuthPhoneInputSectionPreview() {
    LazyPizzaTheme {
        AuthPhoneInputSection(
            state = AuthState(),
            onAction = {},
            isMobilePortrait = true,
            activity = null
        )
    }
}