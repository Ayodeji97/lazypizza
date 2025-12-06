@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.orderhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaCenteredTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaEmptyScreen
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.lazypizza.product.presentation.orderhistory.components.OrderHistoryList
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderItemUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi

@Composable
fun OrderHistoryRoot(
    viewModel: OrderHistoryViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OrderHistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun OrderHistoryScreen(
    state: OrderHistoryState,
    onAction: (OrderHistoryAction) -> Unit,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaCenteredTopAppBar(
                title = stringResource(R.string.order_history_title)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                state.isUnauthorized -> {
                    LazyPizzaEmptyScreen(
                        title = stringResource(R.string.not_signed_in_title),
                        description = stringResource(R.string.not_signed_in_subtitle),
                        buttonText = stringResource(R.string.signed_in_button),
                        onButtonClick = { onAction(OrderHistoryAction.SignIn) },
                        isLoading = false,
                        enabled = true
                    )
                }

                state.isEmpty -> {
                    LazyPizzaEmptyScreen(
                        title = stringResource(R.string.empty_order_title),
                        description = stringResource(R.string.empty_order_description),
                        buttonText = stringResource(R.string.go_to_menu_btn),
                        onButtonClick = { onAction(OrderHistoryAction.GoToMenu) },
                        isLoading = false,
                        enabled = true
                    )
                }

                state.hasOrders -> {
                    OrderHistoryList(
                        orders = state.orders,
                        deviceScreenType = deviceScreenType,
                        modifier = Modifier
                    )
                }
            }
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
        OrderHistoryScreen(
            state = OrderHistoryState(
                isAuthenticated = true,
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
                )
            ),
            onAction = {}
        )
    }
}