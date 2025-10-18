package com.danzucker.lazypizza.product.presentation.productlist

import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

data class ProductListState(
    val isLoadingData: Boolean = false,
    val customerPhoneNumber: UiText = UiText.DynamicString(""),
    val searchQuery: String = "",
    val allProducts: List<LazyPizzaProductListUi> = emptyList(),
    val filteredProducts: List<LazyPizzaProductListUi> = emptyList(),
    val selectedCategories: Set<String> = emptySet(),
    val categories: List<String> = emptyList(),
    val error: UiText? = null
) {
    val hasProducts: Boolean
        get() = filteredProducts.isNotEmpty()
}