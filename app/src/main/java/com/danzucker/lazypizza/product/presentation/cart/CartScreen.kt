@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.cart

import androidx.compose.foundation.layout.Column
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
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi


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
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = DeviceScreenType.fromWindowSizeClass(windowClass)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaCenteredTopAppBar(
                title = stringResource(R.string.cart_title)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                !state.hasProducts -> {
                    LazyPizzaEmptyScreen(
                        title = stringResource(R.string.cart_empty_title),
                        description = stringResource(R.string.cart_empty_subtitle),
                        buttonText = stringResource(R.string.back_to_menu_button),
                        onButtonClick = { onAction(CartAction.BackToMenu) },
                        isLoading = false,
                        enabled = true
                    )
                }
                else -> {
                    if (deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT) {
                        CartPortraitContent(
                            state = state,
                            onAction = onAction
                        )
                    } else {
                        CartLandscapeContent(
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun CartScreenEmptyPreview() {
    LazyPizzaTheme {
        CartScreen(
            state = CartState(
                cartItems = emptyList(),
                recommendedAddOns = emptyList(),
                totalAmount = 0.0
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun CartScreenWithItemsPreview() {
    LazyPizzaTheme {
        CartScreen(
            state = CartState(
                cartItems = listOf(
                    CartItemUi(
                        id = "1",
                        name = "Margherita",
                        imageUrl = "",
                        quantity = 2,
                        unitPrice = 10.99,
                        totalPrice = 21.98,
                        toppings = mapOf(
                            "Extra Cheese" to 1,
                            "Olives" to 2
                        )
                    ),
                    CartItemUi(
                        id = "2",
                        name = "Pepsi",
                        imageUrl = "",
                        quantity = 2,
                        unitPrice = 1.99,
                        totalPrice = 3.98,
                        toppings = emptyMap()
                    ),
                    CartItemUi(
                        id = "3",
                        name = "Cookies Ice-Cream",
                        imageUrl = "",
                        quantity = 1,
                        unitPrice = 1.49,
                        totalPrice = 1.49,
                        toppings = emptyMap()
                    )
                ),
                recommendedAddOns = listOf(
                    RecommendedAddOnUi(
                        id = "r1",
                        name = "BBQ Sauce",
                        price = 0.59,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r2",
                        name = "Garlic Sauce",
                        price = 0.59,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r3",
                        name = "Vanilla",
                        price = 2.49,
                        imageUrl = ""
                    )
                ),
                totalAmount = 27.45
            ),
            onAction = {}
        )
    }
}
