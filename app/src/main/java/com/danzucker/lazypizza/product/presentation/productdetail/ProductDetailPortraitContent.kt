package com.danzucker.lazypizza.product.presentation.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaMiniGridList
import com.danzucker.lazypizza.product.presentation.models.MiniCardInfo

@Composable
fun ProductDetailPortraitContent(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        bottomEnd = 12.dp
                    )
                )
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.margherita),
                contentDescription = stringResource(R.string.lazy_pizza),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
        }

        LazyPizzaBackground(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = state.pizzaDetail?.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.pizzaDetail?.ingredients.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceTint,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.topping_extras),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Toppings list
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
                }
            )
        }
    }
}

