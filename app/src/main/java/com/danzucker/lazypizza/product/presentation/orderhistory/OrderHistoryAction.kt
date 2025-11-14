package com.danzucker.lazypizza.product.presentation.orderhistory

sealed interface OrderHistoryAction {
    object SignIn : OrderHistoryAction
}