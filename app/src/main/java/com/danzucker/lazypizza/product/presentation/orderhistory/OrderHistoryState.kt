package com.danzucker.lazypizza.product.presentation.orderhistory

data class OrderHistoryState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)