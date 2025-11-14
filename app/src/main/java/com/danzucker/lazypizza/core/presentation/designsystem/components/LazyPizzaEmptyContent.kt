package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimarySmallButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme


@Composable
fun LazyPizzaEmptyScreen(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LazyPizzaEmptyContent(
            title = title,
            description = description,
            buttonText = buttonText,
            onButtonClick = onButtonClick,
            isLoading = isLoading,
            enabled = enabled
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}
@Composable
fun LazyPizzaEmptyContent(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surfaceTint
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimarySmallButton(
            text = buttonText,
            onClick = onButtonClick,
            isLoading = isLoading,
            enabled = enabled,
            modifier = Modifier
        )
    }
    
}

@Preview
@Composable
private fun LazyPizzaEmptyScreenPreView() {
    LazyPizzaTheme { 
        LazyPizzaEmptyContent(
            title = "Not Sign In",
            description = "Please sign in to continue",
            buttonText = "Sign In",
            onButtonClick = {},
            isLoading = false,
            enabled = true
        )
    }
}