package com.danzucker.lazypizza.product.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.cart.component.CartItemCard
import com.danzucker.lazypizza.product.presentation.cart.component.CartProductList
import com.danzucker.lazypizza.product.presentation.cart.component.RecommendedAddOnsSection
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.components.StickyBottomBar
import com.danzucker.lazypizza.product.presentation.util.formatAmount

/**
 * Cart portrait content with proper scrolling
 *
 * Structure:
 * - LazyColumn (scrollable): Cart items + Recommended section
 * - StickyBottomBar (always visible): Checkout button
 */
@Composable
fun CartPortraitContent(
    state: CartState,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Scrollable content: Cart items + Recommended add-ons
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cart items
            items(
                items = state.cartItems,
                key = { it.id }
            ) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    onQuantityChange = { newQuantity ->
                        onAction(CartAction.OnQuantityChange(cartItem.id, newQuantity))
                    },
                    onDelete = {
                        onAction(CartAction.OnDeleteItem(cartItem.id))
                    },
                    isMobilePortrait = true
                )
            }

            // Recommended add-ons section (inside scrollable area)
            if (state.recommendedAddOns.isNotEmpty()) {
                item {
                    RecommendedAddOnsSection(
                        recommendedAddOns = state.recommendedAddOns,
                        onAddClick = { addOnId ->
                            onAction(CartAction.OnAddRecommendedItem(addOnId))
                        }
                    )
                }
            }
        }

        // Sticky bottom button (always visible, not scrollable)
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

@Preview
@Composable
private fun CartPortraitContentPreview() {
    LazyPizzaTheme {
        CartPortraitContent(
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

