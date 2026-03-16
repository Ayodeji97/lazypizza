package com.danzucker.lazypizza.product.presentation.orderconfirmation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OrderInfoCard(
    orderNumber: String,
    pickupTime: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        border = BorderStroke(
            color = MaterialTheme.colorScheme.outlineVariant,
            width = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            OrderInfoRow(
                label = stringResource(R.string.order_number_label).uppercase(),
                value = orderNumber
            )
            Spacer(modifier = Modifier.height(6.dp))

            OrderInfoRow(
                label = stringResource(R.string.pickup_time_label).uppercase(),
                value = pickupTime
            )
        }
    }
}


@Composable
fun OrderInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.surfaceTint
        )

        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview
@Composable
fun OrderInfoCardPreview() {
    LazyPizzaTheme {
        OrderInfoCard(
            orderNumber = "#12345",
            pickupTime = "September 25, 12:15",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }
}