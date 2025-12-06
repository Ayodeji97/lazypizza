package com.danzucker.lazypizza.product.presentation.orderhistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaOrderStatusGreenColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaOrderStatusRedColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaOrderStatusOrangeColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi

@Composable
fun OrderStatusBadge(
    status: OrderStatusUi,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        OrderStatusUi.IN_PROGRESS -> LazyPizzaOrderStatusOrangeColor
        OrderStatusUi.COMPLETED -> LazyPizzaOrderStatusGreenColor
        OrderStatusUi.CANCELLED -> LazyPizzaOrderStatusRedColor
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(100)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.displayName.asString(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 10.sp
        )
    }
}


@Preview
@Composable
private fun OrderStatusBadgePreview() {
    LazyPizzaTheme {
        OrderStatusBadge(
            status = OrderStatusUi.IN_PROGRESS,
            modifier = Modifier.padding(16.dp)
        )
    }
}