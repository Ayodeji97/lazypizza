package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaListItem
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

@Composable
fun OrderDetailsList(
    items: List<LazyPizzaProductListUi>,
    onProductClick: (String) -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onDeleteItem: (String) -> Unit,
    deviceScreenType: DeviceScreenType,
    modifier: Modifier = Modifier
) {

    val isMobilePortrait = deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT
    val columnCount = when (deviceScreenType) {
        DeviceScreenType.MOBILE_PORTRAIT -> 1
        else -> 2
    }

    if (isMobilePortrait) {
        Column {  // Single column for mobile
            items.forEach { item ->
                LazyPizzaListItem(
                    lazyPizzaUi = item,
                    onClick = { onProductClick(item.id) },
                    onAddToCart = { /* Not needed in checkout */ },
                    onQuantityChange = { quantity ->
                        onQuantityChange(item.id, quantity)
                    },
                    onDelete = { onDeleteItem(item.id) },
                    isMobilePortrait = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        Column {  // 2-column grid for tablets
            items.chunked(2).forEach { rowItems ->
                Row {
                    rowItems.forEach { item ->
                        LazyPizzaListItem(
                            lazyPizzaUi = item,
                            onClick = { onProductClick(item.id) },
                            onAddToCart = { /* Not needed in checkout */ },
                            onQuantityChange = { quantity ->
                                onQuantityChange(item.id, quantity)
                            },
                            onDelete = { onDeleteItem(item.id) },
                            isMobilePortrait = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(Modifier.weight(1f))  // Fill space
                    }
                }
            }
        }
    }

//    LazyVerticalGrid(
//        columns = GridCells.Fixed(columnCount),
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        contentPadding = PaddingValues(0.dp)
//    ) {
//        items(
//            items = items,
//            key = { item -> item.id }
//        ) { item ->
//            LazyPizzaListItem(
//                lazyPizzaUi = item,
//                onClick = { onProductClick(item.id) },
//                onAddToCart = { /* Not needed in checkout */ },
//                onQuantityChange = { quantity ->
//                    onQuantityChange(item.id, quantity)
//                },
//                onDelete = { onDeleteItem(item.id) },
//                isMobilePortrait = isMobilePortrait,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
}

@Preview
@Composable
private fun OrderDetailsListPreview() {
    LazyPizzaTheme {
        OrderDetailsList(
            items = emptyList(),
            onProductClick = {},
            onDeleteItem = {},
            onQuantityChange = { _, _ -> },
            deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT
        )
    }
}
