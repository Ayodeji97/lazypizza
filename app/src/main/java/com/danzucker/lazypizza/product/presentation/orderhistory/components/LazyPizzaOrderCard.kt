package com.danzucker.lazypizza.product.presentation.orderhistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderItemUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi

@Composable
fun LazyPizzaOrderCard(
    order: OrderUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevationLarge,
                shape = RoundedCornerShape(12.dp),
                spotColor = LazyPizzaShadowColor,
                ambientColor = LazyPizzaShadowColor
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(IntrinsicSize.Min),   // <- so right column can fill height
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // LEFT SIDE: title, date, items
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Order ${order.orderNumber}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = order.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surfaceTint
                )

                Spacer(Modifier.height(16.dp))

                order.items.forEach { item ->
                    Text(
                        text = item.displayText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.size(16.dp))

            // RIGHT SIDE: badge (top) + total (bottom)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                OrderStatusBadge(status = order.status)

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_amount_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                    Text(
                        text = order.totalAmount,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun OrderCardPreview() {
    LazyPizzaTheme {
        LazyPizzaOrderCard(
            order = OrderUi(
                id = "1",
                orderNumber = "#12347",
                date = "September 25, 12:15",
                items = listOf(
                    OrderItemUi("Margherita", 1),
                    OrderItemUi("Margherita", 1),
                    OrderItemUi("Margherita", 1)
                ),
                totalAmount = "$8.99",
                status = OrderStatusUi.IN_PROGRESS
            ),
            modifier = Modifier.padding(40.dp),
            onClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun OrderCardCompletedPreview() {
    LazyPizzaTheme {
        LazyPizzaOrderCard(
            order = OrderUi(
                id = "2",
                orderNumber = "#12346",
                date = "September 25, 12:15",
                items = listOf(
                    OrderItemUi("Margherita", 1),
                    OrderItemUi("Pepsi", 2),
                    OrderItemUi("Cookies Ice Cream", 2)
                ),
                totalAmount = "$25.45",
                status = OrderStatusUi.COMPLETED
            ),
            modifier = Modifier.padding(16.dp),
            onClick = {}
        )
    }
}