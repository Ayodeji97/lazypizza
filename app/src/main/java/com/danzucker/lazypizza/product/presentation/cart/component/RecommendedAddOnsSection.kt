package com.danzucker.lazypizza.product.presentation.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi

@Composable
fun RecommendedAddOnsSection(
    recommendedAddOns: List<RecommendedAddOnUi>,
    onAddClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (recommendedAddOns.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.recommended_to_add_to_your_order).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = recommendedAddOns,
                key = { it.id }
            ) { addOn ->
                RecommendedAddOnCard(
                    addOn = addOn,
                    onAddClick = {
                        onAddClick(addOn.id)
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun RecommendedAddOnsSectionPreview() {
    LazyPizzaTheme {
        RecommendedAddOnsSection(
            recommendedAddOns = listOf(
                RecommendedAddOnUi(
                    id = "1",
                    name = "BBQ Sauce",
                    price = 0.59,
                    imageUrl = ""
                ),
                RecommendedAddOnUi(
                    id = "2",
                    name = "Garlic Sauce",
                    price = 0.59,
                    imageUrl = ""
                ),
                RecommendedAddOnUi(
                    id = "3",
                    name = "Vanilla Shake",
                    price = 2.49,
                    imageUrl = ""
                )
            ),
            onAddClick = {}
        )
    }
}