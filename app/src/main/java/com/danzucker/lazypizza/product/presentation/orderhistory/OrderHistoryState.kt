package com.danzucker.lazypizza.product.presentation.orderhistory

import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi

data class OrderHistoryState(
    val isAuthenticated: Boolean = false,
    val isLoadingData: Boolean = false,
    val orders: List<OrderUi> = emptyList(),
    val errorMessage: String? = null
) {
    val hasOrders: Boolean
        get() = orders.isNotEmpty()

    val isEmpty: Boolean
        get() = isAuthenticated && orders.isEmpty()

    val isUnauthorized: Boolean
        get() = !isAuthenticated
}