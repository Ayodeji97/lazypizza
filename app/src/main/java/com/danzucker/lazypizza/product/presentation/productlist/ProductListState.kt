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
    val error: UiText? = null,
    val showLogoutDialog: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isAnonymous: Boolean = false,
    val userPhoneNumber: String? = null
) {
    val hasProducts: Boolean
        get() = filteredProducts.isNotEmpty()
}