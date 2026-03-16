package com.danzucker.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.danzucker.lazypizza.auth.presentation.auth.AuthRoot
import com.danzucker.lazypizza.product.presentation.checkout.CheckoutRoot
import com.danzucker.lazypizza.product.presentation.orderconfirmation.OrderConfirmationRoot
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
                onNavigateToCheckout = {
                    navController.navigate(NavigationRoute.Checkout)
                }
            )
        }

        composable<NavigationRoute.ProductDetails> {
            ProductDetailRoot(
                onNavigateBack = navController::navigateUp
            )
        }

        composable<NavigationRoute.Checkout> {
            CheckoutRoot(
                onNavigateBack = navController::navigateUp,
                onNavigateToOrderConfirmation = { orderId, orderNumber, pickupTime ->
                    navController.navigate(
                        NavigationRoute.OrderConfirmation(
                            orderId = orderId,
                            orderNumber = orderNumber,
                            pickupTime = pickupTime
                        )
                    ) {
                        // Remove Checkout from back stack
                        popUpTo(NavigationRoute.Checkout) { inclusive = true }
                    }
                }
            )
        }

        // Order Confirmation Screen
        composable<NavigationRoute.OrderConfirmation> {
            val args = it.toRoute<NavigationRoute.OrderConfirmation>()
            OrderConfirmationRoot(
                orderId = args.orderId,
                orderNumber = args.orderNumber,
                pickupTime = args.pickupTime,
                onBackToMenu = {
                    // Navigate back to main screen and clear entire back stack
                    navController.navigate(NavigationRoute.ProductList) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}