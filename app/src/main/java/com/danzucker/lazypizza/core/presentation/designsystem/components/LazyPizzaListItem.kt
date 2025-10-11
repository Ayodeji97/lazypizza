package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme


@Composable
fun LazyPizzaListItem(
    lazyPizzaUi: LazyPizzaUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (lazyPizzaUi.cardType) {
        LazyPizzaCardType.PIZZA -> {
            LazyPizzaCard(
                lazyPizzaUi = lazyPizzaUi,
                onClick = onClick,
                modifier = modifier
            )
        }

        LazyPizzaCardType.OTHERS -> {
            LazyPizzaOtherProductCard(
                lazyPizzaUi = lazyPizzaUi,
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

data class LazyPizzaUi(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val category: String,
    val rating: Float,
    val reviewsCount: Int,
    val isFavorite: Boolean,
    val cardType: LazyPizzaCardType = LazyPizzaCardType.OTHERS
)

@Preview
@Composable
private fun LazyPizzaListItemPreview() {
    LazyPizzaTheme {
        LazyPizzaListItem(
            lazyPizzaUi = LazyPizzaUi(
                id = 1,
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