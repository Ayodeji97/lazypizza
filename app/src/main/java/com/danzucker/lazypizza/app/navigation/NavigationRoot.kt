package com.danzucker.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danzucker.lazypizza.auth.presentation.auth.AuthRoot
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.ProductList
    ) {

        // Auth Screen
        composable<NavigationRoute.Auth> {
            AuthRoot(
                onNavigateToHome = {
                    // Navigate to home and clear auth from back stack
                    navController.navigate(NavigationRoute.ProductList) {
                        popUpTo(NavigationRoute.Auth) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        // Main Scaffold (Home with tabs)
        composable<NavigationRoute.ProductList> {
            MainScaffold(
                onNavigateToProductDetails = { productId ->
                    navController.navigate(
                        NavigationRoute.ProductDetails(
                            productId = productId
                        )
                    )
                },
                onNavigateToAuth = {
                    navController.navigate(NavigationRoute.Auth)
                },
            )
        }

        composable<NavigationRoute.ProductDetails> {
            ProductDetailRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}