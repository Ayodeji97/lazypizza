package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
    if (isMobilePortrait) {
        Column(
            modifier = modifier
        ) {  // Single column for mobile
            items.forEach { item ->
                Spacer(modifier = Modifier.height(8.dp))
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
        Column(
            modifier = modifier
        ) {  // 2-column grid for tablets
            items.chunked(2).forEach { rowItems ->
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    rowItems.forEach { item ->
                        Spacer(modifier = Modifier.width(8.dp))
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
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
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
