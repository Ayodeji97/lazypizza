package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaCategoryChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    Surface(
        onClick = onClick,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        modifier = modifier
            .height(32.dp)
            .wrapContentWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

}

@Preview
@Composable
private fun LazyPizzaCategoryListItemPreview() {
    LazyPizzaTheme {
        LazyPizzaCategoryChip(
            text = "Pizza",
            onClick = { },
            selected = false,
            modifier = Modifier.padding(30.dp)
        )
    }
}