package com.danzucker.lazypizza.product.presentation.orderconfirmation

data class OrderConfirmationState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)