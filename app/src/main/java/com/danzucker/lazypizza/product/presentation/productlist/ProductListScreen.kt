@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.productlist

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.components.SearchBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.ObserveAsEvents
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaCategoryChipList
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaListProductList
import androidx.core.net.toUri
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaAlertDialog
import com.danzucker.lazypizza.product.presentation.components.UserIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListRoot(
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: ProductListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ProductListEvent.OpenPhoneDialer -> {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:${event.phoneNumber}".toUri()
                }
                context.startActivity(intent)
            }

            is ProductListEvent.ShowErrorMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            ProductListEvent.ItemAddedToCart -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.item_added_to_cart),
                    Toast.LENGTH_LONG
                ).show()
            }
            ProductListEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    ProductListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ProductListAction.OnProductClick -> {
                    onNavigateToProductDetails(action.productId)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ProductListScreen(
    state: ProductListState,
    onAction: (ProductListAction) -> Unit,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaTopAppBar(
                title = stringResource(R.string.lazy_pizza),
                customerPhoneNumber = state.userPhoneNumber,
                onCustomerPhoneNumberClick = {
                    onAction(ProductListAction.OnPhoneNumberClick)
                },
                startContent = {
                    Image(
                        painter = painterResource(R.drawable.lazy_pizza_logo),
                        contentDescription = stringResource(R.string.lazy_pizza),
                        modifier = Modifier
                            .size(20.dp)
                    )
                },
                endContent = {
                    UserIcon(
                        isAuthenticated = state.isAuthenticated,
                        isAnonymous = state.isAnonymous,
                        onClick = { onAction(ProductListAction.OnUserIconClick) }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = stringResource(R.string.lazy_pizza),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT) 160.dp else 200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                searchText = state.searchQuery,
                onSearchTextChange = { query ->
                    onAction(ProductListAction.OnSearchQueryChange(query))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyPizzaCategoryChipList(
                categories = state.categories,
                selectedCategories = state.selectedCategory?.let { setOf(it) } ?: emptySet(),
                onCategorySelected = { category ->
                    onAction(ProductListAction.OnCategorySelected(category))
                }
            )

            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                !state.hasProducts -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                state.showLogoutDialog -> {
                    LazyPizzaAlertDialog(
                        title = stringResource(R.string.logout_confirmation),
                        confirmText = stringResource(R.string.logout),
                        onConfirmClick = {
                            onAction(ProductListAction.OnLogoutConfirmed)
                        },
                        onDismissClick = {
                            onAction(ProductListAction.OnLogoutCancelled)
                        }
                    )
                }
                else -> {
                    LazyPizzaListProductList(
                        lazyPizzas = state.filteredProducts,
                        deviceScreenType = DeviceScreenType.fromWindowSizeClass(windowClass),
                        scrollToCategory = state.selectedCategory,
                        onScrollComplete = {
                            onAction(ProductListAction.OnScrollToCategoryComplete)
                        },
                        onProductClick = { pizzaId ->
                            onAction(ProductListAction.OnProductClick(pizzaId))
                        },
                        onAddToCartClick = { productId ->
                            onAction(ProductListAction.OnAddToCart(productId))
                        },
                        onQuantityChange = { productId, quantity ->
                            onAction(ProductListAction.OnQuantityChange(productId, quantity))
                        },
                        onDeleteClick = { productId ->
                            onAction(ProductListAction.OnDeleteFromCart(productId))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductListScreenPreview() {
    LazyPizzaTheme {
        ProductListScreen(
            state = ProductListState(),
            onAction = {}
        )
    }
}