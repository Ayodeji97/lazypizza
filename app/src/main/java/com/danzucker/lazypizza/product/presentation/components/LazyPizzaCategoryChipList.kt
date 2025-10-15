package com.danzucker.lazypizza.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun LazyPizzaCategoryChipList(
    categories: List<String>,
    selectedCategories: Set<String>,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = categories,
        ) { category ->
            LazyPizzaCategoryChip(
                text = category,
                onClick = {
                    onCategorySelected(category)
                },
                selected = category in selectedCategories
            )
        }
    }
}


@Preview
@Composable
private fun LazyPizzaCategoryChipListPreview() {
    LazyPizzaTheme {
        LazyPizzaCategoryChipList(
            categories = listOf(
                "All", "Vegetarian", "Non-Vegetarian", "Vegan", "Gluten-Free"
            ),
            selectedCategories = setOf("Pizza"),
            onCategorySelected = {},
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }

}