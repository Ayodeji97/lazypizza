package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

@Composable
fun OrderDetailsSection(
    items: List<LazyPizzaProductListUi>,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onProductClick: (String) -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onDeleteItem: (String) -> Unit,
    modifier: Modifier = Modifier,
    deviceScreenType: DeviceScreenType = DeviceScreenType.MOBILE_PORTRAIT
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OrderDetailsHeader(
            isExpanded = isExpanded,
            onExpandToggle = onExpandToggle,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OrderDetailsList(
                items = items,
                onProductClick = onProductClick,
                onQuantityChange = onQuantityChange,
                onDeleteItem = onDeleteItem,
                deviceScreenType = deviceScreenType,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(name = "Collapsed")
@Composable
private fun OrderDetailsSectionCollapsedPreview() {
    LazyPizzaTheme {
        OrderDetailsSection(
            items = listOf(
                LazyPizzaProductListUi(
                    id = "1",
                    name = "Margherita",
                    description = "Classic pizza",
                    price = "$8.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Pizza",
                    rating = 4.5f,
                    reviewsCount = 150,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "2",
                    name = "Pepsi",
                    description = "Refreshing beverage",
                    price = "$1.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Beverage",
                    rating = 4.0f,
                    reviewsCount = 50,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                )
            ),
            isExpanded = false,
            onExpandToggle = {},
            onProductClick = {},
            onQuantityChange = { _, _ -> },
            onDeleteItem = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Expanded - Mobile")
@Composable
private fun OrderDetailsSectionExpandedMobilePreview() {
    LazyPizzaTheme {
        OrderDetailsSection(
            items = listOf(
                LazyPizzaProductListUi(
                    id = "1",
                    name = "Margherita",
                    description = "Classic pizza",
                    price = "$8.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Pizza",
                    rating = 4.5f,
                    reviewsCount = 150,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "2",
                    name = "Pepsi",
                    description = "Refreshing beverage",
                    price = "$1.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Beverage",
                    rating = 4.0f,
                    reviewsCount = 50,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "3",
                    name = "Cookies Ice Cream",
                    description = "Delicious dessert",
                    price = "$1.49",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Dessert",
                    rating = 4.8f,
                    reviewsCount = 200,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 1
                )
            ),
            isExpanded = true,
            onExpandToggle = {},
            onProductClick = {},
            onQuantityChange = { _, _ -> },
            onDeleteItem = {},
            deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Expanded - Tablet", widthDp = 840)
@Composable
private fun OrderDetailsSectionExpandedTabletPreview() {
    LazyPizzaTheme {
        OrderDetailsSection(
            items = listOf(
                LazyPizzaProductListUi(
                    id = "1",
                    name = "Margherita",
                    description = "Classic pizza",
                    price = "$8.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Pizza",
                    rating = 4.5f,
                    reviewsCount = 150,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "2",
                    name = "Pepperoni",
                    description = "Spicy pizza",
                    price = "$9.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Pizza",
                    rating = 4.7f,
                    reviewsCount = 180,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "3",
                    name = "Pepsi",
                    description = "Refreshing beverage",
                    price = "$1.99",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Beverage",
                    rating = 4.0f,
                    reviewsCount = 50,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 2
                ),
                LazyPizzaProductListUi(
                    id = "4",
                    name = "Cookies Ice Cream",
                    description = "Delicious dessert",
                    price = "$1.49",
                    imageUrl = "",
                    isAvailable = true,
                    category = "Dessert",
                    rating = 4.8f,
                    reviewsCount = 200,
                    isFavorite = false,
                    cardType = LazyPizzaCardType.OTHERS,
                    quantityInCart = 1
                )
            ),
            isExpanded = true,
            onExpandToggle = {},
            onProductClick = {},
            onQuantityChange = { _, _ -> },
            onDeleteItem = {},
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            modifier = Modifier.padding(16.dp)
        )
    }
}