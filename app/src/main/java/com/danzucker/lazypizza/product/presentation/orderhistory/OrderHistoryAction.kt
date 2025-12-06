package com.danzucker.lazypizza.product.presentation.orderhistory

sealed interface OrderHistoryAction {
    object SignIn : OrderHistoryAction
    data object GoToMenu : OrderHistoryAction
    data object Refresh : OrderHistoryAction
}