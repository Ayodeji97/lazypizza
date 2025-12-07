package com.danzucker.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danzucker.lazypizza.auth.presentation.auth.AuthRoot
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val cartRepository: CartRepository = koinInject()
    val cartItemCount by cartRepository.getCartItemsCount()
        .collectAsStateWithLifecycle(initialValue = 0)

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
                cartItemCount = cartItemCount
            )
        }

        composable<NavigationRoute.ProductDetails> {
            ProductDetailRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}