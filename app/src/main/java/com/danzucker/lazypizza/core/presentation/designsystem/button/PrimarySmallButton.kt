package com.danzucker.lazypizza.core.presentation.designsystem.button


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaButtonGradient
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaButtonTransparentGradient
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.paddingSmallMedium12

@Composable
fun PrimarySmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {

    Box(
        modifier = modifier
            .background(
                brush = if (enabled && !isLoading) {
                    MaterialTheme.colorScheme.LazyPizzaButtonGradient
                } else {
                    MaterialTheme.colorScheme.LazyPizzaButtonTransparentGradient
                },
                shape = RoundedCornerShape(100)
            )
    ) {
        Button(
            onClick = onClick,
            enabled = enabled && !isLoading,
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            ),
            contentPadding = PaddingValues(
                horizontal = paddingLarge24,
                vertical = paddingSmallMedium12
            )
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(15.dp),
                    strokeWidth = 1.5.dp,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun PrimarySmallButtonPreview() {
    LazyPizzaTheme {
        PrimarySmallButton(
            text = "",
            onClick = {},
            enabled = true,
            isLoading = false,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}