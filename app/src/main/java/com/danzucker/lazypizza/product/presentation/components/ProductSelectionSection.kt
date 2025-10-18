package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.MinusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.PlusIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.CardShell

@Composable
fun ProductSelectionSection(
    quantity: String,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableIncreaseButton: Boolean = true,
    enableDecreaseButton: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        CardShell(
            onClick = onDecreaseClick,
            enabled = enableDecreaseButton
        ) {
            Icon(
                imageVector = MinusIcon,
                contentDescription = null,
                tint = if (enableDecreaseButton) {
                    MaterialTheme.colorScheme.surfaceTint
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                modifier = Modifier
                    .size(14.dp)
            )
        }

        Text(
            text = quantity,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        CardShell(
            onClick = onIncreaseClick,
            enabled = enableIncreaseButton
        ) {
            Icon(
                imageVector = PlusIcon,
                contentDescription = null,
                tint = if (enableIncreaseButton) {
                    MaterialTheme.colorScheme.surfaceTint
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                modifier = Modifier
                    .size(14.dp)
            )
        }
    }
}