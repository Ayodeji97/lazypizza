package com.danzucker.lazypizza.product.presentation.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi

@Composable
fun CartProductList(
    cartItems: List<CartItemUi>,
    onQuantityChange: (String, Int) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isMobilePortrait: Boolean = true
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = cartItems,
            key = { it.id }
        ) { cartItem ->
            CartItemCard(
                cartItem = cartItem,
                onQuantityChange = { newQuantity ->
                    onQuantityChange(cartItem.id, newQuantity)
                },
                onDelete = {
                    onDeleteClick(cartItem.id)
                },
                isMobilePortrait = isMobilePortrait
            )
        }
    }
}


@Preview
@Composable
private fun CartProductListPreview() {
    LazyPizzaTheme {
        CartProductList(
            cartItems = listOf(
                CartItemUi(
                    id = "1",
                    name = "Margherita",
                    imageUrl = "",
                    quantity = 2,
                    unitPrice = 10.99,
                    totalPrice = 21.98,
                    toppings = mapOf(
                        "Extra Cheese" to 1
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
                )
            ),
            onQuantityChange = { _, _ -> },
            onDeleteClick = {}
        )
    }
}