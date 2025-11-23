package com.danzucker.lazypizza.product.presentation.productlist

import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

data class ProductListState(
    val allProducts: List<LazyPizzaProductListUi> = emptyList(),
    val filteredProducts: List<LazyPizzaProductListUi> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val isLoadingData: Boolean = false,
    val customerPhoneNumber: UiText = UiText.DynamicString(""),
    val error: UiText? = null,
    val cartItemsCount: Int = 0
) {
    val hasProducts: Boolean
        get() = filteredProducts.isNotEmpty()
}