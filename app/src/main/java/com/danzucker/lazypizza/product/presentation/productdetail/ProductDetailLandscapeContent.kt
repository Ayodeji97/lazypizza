package com.danzucker.lazypizza.product.presentation.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.components.RemoteImage
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaMiniGridList
import com.danzucker.lazypizza.product.presentation.components.StickyBottomBar
import com.danzucker.lazypizza.product.presentation.models.MiniCardInfo

@Composable
fun ProductDetailLandscapeContent(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                RemoteImage(
                    imageUrl = state.pizzaDetail?.imageUrl ?: "",
                    contentDescription = state.pizzaDetail?.name,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.pizzaDetail?.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.pizzaDetail?.ingredients.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
            )
        }

        // Right side - Toppings
        LazyPizzaBackground(
            bottomStartCornerRadius = 16.dp,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.topping_extras),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyPizzaMiniGridList(
                miniToppings = state.availableToppings.map { topping ->
                    MiniCardInfo(
                        id = topping.id,
                        title = topping.name,
                        price = topping.price,
                        imageUrl = topping.imageUrl,
                        quantity = state.selectedToppings[topping.id] ?: 0
                    )
                },
                onToppingClick = { toppingId ->
                    onAction(ProductDetailAction.OnToppingClick(toppingId))
                },
                onQuantityChange = { toppingId, quantity ->
                    onAction(
                        ProductDetailAction.OnToppingQuantityChange(
                            toppingId,
                            quantity
                        )
                    )
                },
                modifier = Modifier
                    .weight(6f)
            )

            StickyBottomBar(
                buttonText = stringResource(
                    R.string.add_to_cart_button_text,
                    state.formattedTotalPrice
                ),
                onButtonClick = {
                    onAction(ProductDetailAction.OnAddToCartClick)
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }

}

@Preview(widthDp = 840, heightDp = 480)
@Composable
private fun ProductDetailLandscapeContentPreview() {
    LazyPizzaTheme {
        ProductDetailLandscapeContent(
            state = ProductDetailState(
                pizzaDetail = null
            ),
            onAction = {}
        )
    }
}