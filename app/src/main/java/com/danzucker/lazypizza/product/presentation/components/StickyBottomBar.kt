package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimaryButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun StickyBottomBar(
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 0f,
                    endY = 150f
                )
            )
            .padding(16.dp)
    ) {
        PrimaryButton(
            text = buttonText,
            onClick = onButtonClick,
            isLoading = isLoading,
            enabled = enabled
        )
    }
}


@Preview
@Composable
private fun StickyBottomBarPreview() {
    LazyPizzaTheme {
        StickyBottomBar(
            buttonText = "Add to Cart",
            onButtonClick = {},
        )
    }
}