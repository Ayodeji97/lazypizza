package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

@Composable
fun LazyPizzaListProductList(
    lazyPizzas: List<LazyPizzaProductListUi>,
    deviceScreenType: DeviceScreenType,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollToCategory: String? = null,
    onScrollComplete: () -> Unit = {}
) {
    val columnCount = when (deviceScreenType) {
        MOBILE_PORTRAIT -> 1
        else -> 2
    }

    val groupedPizzas = lazyPizzas.groupBy { it.category }
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(scrollToCategory) {
        if (scrollToCategory != null) {
            val sectionIndex = groupedPizzas.keys.indexOfFirst { it == scrollToCategory }
            if (sectionIndex != -1) {
                // Calculate the item index to scroll to
                val itemIndex = groupedPizzas.entries
                    .take(sectionIndex)
                    .sumOf { it.value.size + 1 } // +1 for the header

                lazyGridState.animateScrollToItem(itemIndex)
                onScrollComplete()
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        state = lazyGridState,
        modifier = modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        groupedPizzas.entries.forEachIndexed { sectionIndex, (category, lazyPizzaItems) ->
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(
                items = lazyPizzaItems,
                key = { pizza -> pizza.id }
            ) { lazyPizzaUi ->
                LazyPizzaListItem(
                    lazyPizzaUi = lazyPizzaUi,
                    isMobilePortrait = deviceScreenType == MOBILE_PORTRAIT,
                    onClick = { onProductClick(lazyPizzaUi.id) },
                    onAddToCart = { onAddToCartClick(lazyPizzaUi.id) },
                    onQuantityChange = { quantity ->
                        onQuantityChange(lazyPizzaUi.id, quantity)
                    },
                    onDelete = { onDeleteClick(lazyPizzaUi.id) },
                    modifier = Modifier
                )
            }
        }
    }
}


@Preview
@Composable
private fun LazyPizzaListProductListPreview() {
    LazyPizzaTheme {
        LazyPizzaListProductList(
            lazyPizzas = List(20) {
                LazyPizzaProductListUi(
                    id = it.toString(),
                    name = "Pizza $it",
                    description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",
                    price = "$${(10..30).random()}",
                    imageUrl = "",
                    isAvailable = true,
                    category = if (it % 2 == 0) "Pizza" else "Beverage",
                    rating = (1..5).random().toFloat(),
                    reviewsCount = (0..100).random(),
                    isFavorite = it % 2 == 0,
                    cardType = if (it % 2 == 0) LazyPizzaCardType.PIZZA else LazyPizzaCardType.OTHERS
                )
            },
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            onProductClick = {},
            onAddToCartClick = {},
            onQuantityChange = { _, _ ->
            },
            onDeleteClick = {},
            modifier = Modifier
        )
    }
}