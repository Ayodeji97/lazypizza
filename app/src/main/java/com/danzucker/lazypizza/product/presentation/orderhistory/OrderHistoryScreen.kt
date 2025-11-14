package com.danzucker.lazypizza.product.presentation.orderhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Order History Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        OrderHistoryScreen(
            state = OrderHistoryState(),
            onAction = {}
        )
    }
}