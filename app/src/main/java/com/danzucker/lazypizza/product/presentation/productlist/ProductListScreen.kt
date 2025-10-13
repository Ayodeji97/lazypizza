@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.components.SearchBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaCategoryChipList
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaListProductList

@Composable
fun ProductListRoot(
    viewModel: ProductViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProductListScreen(
    state: ProductListState,
    onAction: (ProductListAction) -> Unit,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LazyPizzaTopAppBar(
                title = stringResource(R.string.lazy_pizza),
                customerPhoneNumber = state.customerPhoneNumber.asString(),
                onCustomerPhoneNumberClick = {},
                modifier = Modifier
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
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                searchText = "",
                onSearchTextChange = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyPizzaCategoryChipList(
                categories = state.categories.map { it.asString() },
                modifier = Modifier
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

                !state.hasProducts -> {}
                else -> {
                    LazyPizzaListProductList(
                        lazyPizzas = state.products,
                        deviceScreenType = DeviceScreenType.fromWindowSizeClass(windowClass),
                        onPizzaClick = { pizzaId -> }
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