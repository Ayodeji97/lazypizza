package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.ArrowDownIcon
import com.danzucker.lazypizza.core.presentation.designsystem.ArrowUpIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OrderDetailsHeader(
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    modifier: Modifier = Modifier
) {

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Arrow rotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onExpandToggle)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.order_details_subtitle),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surfaceTint
        )

        OrderDetailsIconShell(
            onClick = onExpandToggle,
            color = Color.Transparent
        ) {
            Icon(
                imageVector = if (isExpanded) {
                    ArrowUpIcon
                } else ArrowDownIcon,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = if (isExpanded) {
                    stringResource(R.string.up_arrow)
                } else stringResource(R.string.down_arrow),
                modifier = Modifier
                    .rotate(rotationAngle)
            )
        }
    }
}

@Preview
@Composable
private fun OrderDetailsHeaderPreview() {
    LazyPizzaTheme {
        OrderDetailsHeader(
            isExpanded = true,
            onExpandToggle = {}
        )
    }
}