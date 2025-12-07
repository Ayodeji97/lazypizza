package com.danzucker.lazypizza.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute {

    @Serializable
    data object Auth : NavigationRoute
    @Serializable
    data object ProductList : NavigationRoute

    @Serializable
    data class ProductDetails(val productId: String) : NavigationRoute

    @Serializable
    data object Cart : NavigationRoute

    @Serializable
    data object History : NavigationRoute
}