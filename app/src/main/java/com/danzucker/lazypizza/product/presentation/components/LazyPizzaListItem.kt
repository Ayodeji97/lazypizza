package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi


@Composable
fun LazyPizzaListItem(
    lazyPizzaUi: LazyPizzaProductListUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isMobilePortrait: Boolean = true,
) {
    when (lazyPizzaUi.cardType) {
        LazyPizzaCardType.PIZZA -> {
            LazyPizzaCard(
                lazyPizzaUi = lazyPizzaUi,
                isMobilePortrait = isMobilePortrait,
                onClick = onClick,
                modifier = modifier
            )
        }
        LazyPizzaCardType.OTHERS -> {
            LazyPizzaOtherProductCard(
                lazyPizzaUi = lazyPizzaUi,
                isMobilePortrait = isMobilePortrait,
                onClick = onClick,
                onAddToCart = {},
                onQuantityChange = {},
                onDelete = {},
                quantity = 0,
                modifier = modifier
            )
        }
    }
}

enum class LazyPizzaCardType {
    PIZZA, OTHERS
}

@Preview
@Composable
private fun LazyPizzaListItemPreview() {
    LazyPizzaTheme {
        LazyPizzaListItem(
            lazyPizzaUi = LazyPizzaProductListUi(
                id = "1",
                name = "Margherita",
                description = "Classic delight with 100% real mozzarella cheese",
                price = "$5.99",
                imageUrl = "",
                isAvailable = true,
                category = "Vegetarian",
                rating = 4.5f,
                reviewsCount = 150,
                isFavorite = false
            ),
            onClick = {},
            modifier = Modifier
                .padding(30.dp)
        )
    }
}