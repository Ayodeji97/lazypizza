package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaListItem
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaListUi

@Composable
fun LazyPizzaListProductList(
    lazyPizzas: List<LazyPizzaListUi>,
    deviceScreenType: DeviceScreenType,
    onPizzaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val columnCount = when (deviceScreenType) {
        MOBILE_PORTRAIT -> 1
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(
            items = lazyPizzas,
            key = { pizza -> pizza.id }
        ) { lazyPizzaUi ->
            LazyPizzaListItem(
                lazyPizzaUi = lazyPizzaUi,
                isMobilePortrait = deviceScreenType == MOBILE_PORTRAIT,
                onClick = {
                    onPizzaClick(lazyPizzaUi.id)
                },
                modifier = Modifier
            )
        }

    }

}

@Preview
@Composable
private fun LazyPizzaListProductListPreview() {
    LazyPizzaTheme {
        LazyPizzaListProductList(
            lazyPizzas = List(20) {
                LazyPizzaListUi(
                    id = it.toString(),
                    name = "Pizza $it",
                    description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",
                    price = "$${(10..30).random()}",
                    imageUrl = "",
                    isAvailable = true,
                    category = if (it % 2 == 0) "Pizza" else "Beverage",
                    rating = (1..5).random().toFloat(),
                    reviewsCount = (0..100).random(),
                    isFavorite = it % 2 == 0
                )
            },
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            onPizzaClick = {}
        )
    }
}