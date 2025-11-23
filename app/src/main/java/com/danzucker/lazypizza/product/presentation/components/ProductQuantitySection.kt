package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductQuantitySection(
    quantity: String,
    totalAmount: String,
    price: String,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableIncreaseButton: Boolean = true,
    enableDecreaseButton: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                bottom = 12.dp,
                end = 16.dp
            )
    ) {
        ProductSelectionSection(
            quantity = quantity,
            onDecreaseClick = onDecreaseClick,
            onIncreaseClick = onIncreaseClick,
            enableIncreaseButton = enableIncreaseButton,
            enableDecreaseButton = enableDecreaseButton
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = totalAmount,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "$quantity x $price",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceTint,
            )
        }
    }
}