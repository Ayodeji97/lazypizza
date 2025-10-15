package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaMiniGridList(
    miniToppings: List<MiniCardInfo>,
    onToppingClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(
            items = miniToppings,
            key = { it.id }
        ) { miniTopping ->
            LazyPizzaMiniCard(
                miniCardInfo = miniTopping,
                onClick = {
                    onToppingClick(miniTopping.id)
                },
                onDecreaseClick = {},
                onIncreaseClick = {},
                quantity = "0",
                selected = false
            )
        }
    }
}


@Preview
@Composable
private fun LazyPizzaMiniGridListPreview() {
    LazyPizzaTheme {
        LazyPizzaMiniGridList(
            miniToppings = List(16) {
                MiniCardInfo(
                    id = it.toString(),
                    title = "Topping $it",
                    price = "$${(1..5).random()}.99",
                    imageUrl = "",
                )
            },
            onToppingClick = {}
        )
    }
}