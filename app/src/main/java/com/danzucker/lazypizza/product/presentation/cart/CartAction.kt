package com.danzucker.lazypizza.product.presentation.cart

sealed interface CartAction {
    data object BackToMenu : CartAction
}