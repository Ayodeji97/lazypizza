package com.danzucker.lazypizza.product.presentation.orderhistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.lazypizza.product.presentation.orderhistory.OrderHistoryScreen
import com.danzucker.lazypizza.product.presentation.orderhistory.OrderHistoryState
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderItemUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi

@Composable
fun OrderHistoryList(
    orders: List<OrderUi>,
    deviceScreenType: DeviceScreenType,
    modifier: Modifier = Modifier
) {
    val columnCount = if (deviceScreenType == MOBILE_PORTRAIT) 1 else 2

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columnCount),
        modifier = modifier.fillMaxSize(),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = orders,
            key = { it.id }
        ) { order ->
            LazyPizzaOrderCard(
                order = order,
                onClick = { },
            )
        }
    }
}


@Preview(name = "Unauthorized")
@Composable
private fun OrderHistoryUnauthorizedPreview() {
    LazyPizzaTheme {
        OrderHistoryScreen(
            state = OrderHistoryState(
                isAuthenticated = false
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Empty - Authorized")
@Composable
private fun OrderHistoryEmptyPreview() {
    LazyPizzaTheme {
        OrderHistoryScreen(
            state = OrderHistoryState(
                isAuthenticated = true,
                orders = emptyList()
            ),
            onAction = {}
        )
    }
}

@Preview(name = "With Orders")
@Composable
private fun OrderHistoryWithOrdersPreview() {
    LazyPizzaTheme {
        OrderHistoryList(
            orders = listOf(
                OrderUi(
                    id = "1",
                    orderNumber = "#12347",
                    date = "September 25, 12:15",
                    items = listOf(OrderItemUi("Margherita", 1)),
                    totalAmount = "$8.99",
                    status = OrderStatusUi.IN_PROGRESS
                ),
                OrderUi(
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
                OrderUi(
                    id = "3",
                    orderNumber = "#12345",
                    date = "September 25, 12:15",
                    items = listOf(
                        OrderItemUi("Margherita", 1),
                        OrderItemUi("Cookies Ice Cream", 2)
                    ),
                    totalAmount = "$11.78",
                    status = OrderStatusUi.COMPLETED
                )
            ),
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            modifier = Modifier
        )
    }
}