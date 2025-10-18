package com.danzucker.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.ProductList
    ) {
        composable<NavigationRoute.ProductList> {
            ProductListRoot(
                onNavigateToProductDetails = { productId ->
                    navController.navigate(
                        NavigationRoute.ProductDetails(
                            productId = productId
                        )
                    )
                }
            )
        }

        composable<NavigationRoute.ProductDetails> {
            ProductDetailRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}