package com.danzucker.lazypizza.product.presentation.productlist

import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.util.UiText
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

data class ProductListState(
    val isLoadingData: Boolean = false,
    val hasProducts: Boolean = false,
    val customerPhoneNumber: UiText = UiText.StringResourceWithArgs(R.string.customer_phone_number),
    val products: List<LazyPizzaProductListUi> = emptyList(),
    val categories: List<UiText> = emptyList(),
)