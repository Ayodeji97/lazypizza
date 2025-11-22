package com.danzucker.lazypizza.product.presentation.cart

import android.R.attr.enabled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.cart.component.CartProductList
import com.danzucker.lazypizza.product.presentation.cart.component.RecommendedAddOnsSection
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.components.StickyBottomBar
import com.danzucker.lazypizza.product.presentation.util.formatAmount

@Composable
fun CartLandscapeContent(
    state: CartState,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CartProductList(
            cartItems = state.cartItems,
            onQuantityChange = { itemId, quantity ->
                onAction(CartAction.OnQuantityChange(itemId, quantity))
            },
            onDeleteClick = { itemId ->
                onAction(CartAction.OnDeleteItem(itemId))
            },
            modifier = Modifier.weight(1f),
            isMobilePortrait = false
        )

        LazyPizzaBackground(
            bottomStartCornerRadius = 16.dp,
            modifier = Modifier
                .weight(1f)
        ) {
            if (state.recommendedAddOns.isNotEmpty()) {
                RecommendedAddOnsSection(
                    recommendedAddOns = state.recommendedAddOns,
                    onAddClick = { addOnId ->
                        onAction(CartAction.OnAddRecommendedItem(addOnId))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            StickyBottomBar(
                buttonText = stringResource(
                    R.string.proceed_to_checkout,
                    formatAmount(state.totalAmount)
                ),
                onButtonClick = {
                    onAction(CartAction.OnProceedToCheckout)
                },
                enabled = true
            )

        }
    }
}


@Preview
@Composable
private fun CartLandscapeContentPreview() {
    LazyPizzaTheme {
        CartLandscapeContent(
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
                    ),
                    CartItemUi(
                        id = "4",
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