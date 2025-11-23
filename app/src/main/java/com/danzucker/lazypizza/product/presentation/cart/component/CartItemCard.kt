package com.danzucker.lazypizza.product.presentation.cart.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.DeleteIcon
import com.danzucker.lazypizza.core.presentation.designsystem.components.CardShell
import com.danzucker.lazypizza.core.presentation.designsystem.components.RemoteImage
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaShadowColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi
import com.danzucker.lazypizza.product.presentation.components.ProductQuantitySection
import com.danzucker.lazypizza.product.presentation.util.formatAmount

@Composable
fun CartItemCard(
    cartItem: CartItemUi,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isMobilePortrait: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ),
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .size(if (isMobilePortrait) 120.dp else 140.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .size(if (isMobilePortrait) 120.dp else 140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                RemoteImage(
                    imageUrl = cartItem.imageUrl,
                    contentDescription = cartItem.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = cartItem.name,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (cartItem.toppings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            cartItem.toppings.entries
                                .sortedBy { it.key }
                                .forEach { (topping, quantity) ->
                                    Text(
                                        text = "$quantity × $topping",  // Each on own line
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.surfaceTint
                                    )
                                }
                        }
                    }

                    CardShell(onClick = onDelete) {
                        Icon(
                            imageVector = DeleteIcon,
                            contentDescription = "Delete item",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price and quantity section
                ProductQuantitySection(
                    quantity = cartItem.quantity.toString(),
                    totalAmount = formatAmount(cartItem.totalPrice),
                    price = formatAmount(cartItem.unitPrice),
                    onDecreaseClick = {
                        if (cartItem.quantity > 1) {
                            onQuantityChange(cartItem.quantity - 1)
                        } else {
                            onDelete()
                        }
                    },
                    onIncreaseClick = {
                        onQuantityChange(cartItem.quantity + 1)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Preview
@Composable
fun CartItemCardPreview(modifier: Modifier = Modifier) {
    LazyPizzaTheme {
        CartItemCard(
            cartItem = CartItemUi(
                id = "1",
                name = "Pizza",
                imageUrl = "",
                unitPrice = 10.0,
                totalPrice = 20.0,
                quantity = 2,
                toppings = mapOf(
                    "Pepperoni" to 2,
                    "Mushrooms" to 4,
                    "Sausage" to 1,
                )
            ),
            onQuantityChange = {},
            onDelete = {},
            modifier = modifier,
            isMobilePortrait = false
        )
    }
}

