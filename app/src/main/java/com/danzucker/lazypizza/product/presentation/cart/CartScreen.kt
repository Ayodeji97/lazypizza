@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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


@Composable
fun CartRoot(
    viewModel: CartViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CartScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaCenteredTopAppBar(
                title = stringResource(R.string.cart_title)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyPizzaEmptyScreen(
                title = stringResource(R.string.cart_empty_title),
                description = stringResource(R.string.cart_empty_subtitle),
                buttonText = stringResource(R.string.back_to_menu_button),
                onButtonClick = { onAction(CartAction.BackToMenu) },
                isLoading = false,
                enabled = true
            )
        }
    }

}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        CartScreen(
            state = CartState(),
            onAction = {}
        )
    }
}