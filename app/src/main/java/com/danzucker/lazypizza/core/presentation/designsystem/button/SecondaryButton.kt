package com.danzucker.lazypizza.core.presentation.designsystem.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    LazyPizzaTheme {
        SecondaryButton(
            text = "Back",
            onClick = {},
        )
    }
}
