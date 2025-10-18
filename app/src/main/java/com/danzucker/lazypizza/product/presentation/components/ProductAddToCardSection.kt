package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R

@Composable
fun ProductAddToCardSection(
    price: String,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
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
        Text(
            text = price,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(
            onClick = onAddToCart,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.01f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            ),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = stringResource(R.string.add_to_cart),
            )
        }
    }
}