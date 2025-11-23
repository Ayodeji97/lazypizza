package com.danzucker.lazypizza.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListRoot
import com.danzucker.lazypizza.product.presentation.productlist.ProductListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val productListViewModel: ProductListViewModel = koinViewModel()
    val productListState by productListViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.ProductList
    ) {
        composable<NavigationRoute.ProductList> {
            MainScaffold(
                onNavigateToProductDetails = { productId ->
                    navController.navigate(
                        NavigationRoute.ProductDetails(
                            productId = productId
                        )
                    )
                },
                cartItemCount = productListState.cartItemsCount
            )
        }

        composable<NavigationRoute.ProductDetails> {
            ProductDetailRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}