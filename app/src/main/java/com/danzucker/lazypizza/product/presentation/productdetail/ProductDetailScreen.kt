package com.danzucker.lazypizza.product.presentation.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.BackButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.components.LazyPizzaMiniGridList
import com.danzucker.lazypizza.product.presentation.components.MiniCardInfo
import com.danzucker.lazypizza.product.presentation.components.StickyBottomBar

@Composable
fun ProductDetailRoot(
    viewModel: ProductDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            LazyPizzaTopAppBar(
                navigationIcon = {
                    BackButton(
                        onClick = {},
                        modifier = Modifier.padding(16.dp)
                    )
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.margherita),
                        contentDescription = stringResource(R.string.lazy_pizza),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                }

                LazyPizzaBackground(
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = "Margherita",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tomato sauce, Mozzarella, Fresh basil, Olive oil",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceTint,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Add Extra Toppings",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Toppings list
                    LazyPizzaMiniGridList(
                        miniToppings = listOf(
                            MiniCardInfo(
                                id = "1",
                                title = "Corn",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "2",
                                title = "Mushrooms",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "3",
                                title = "Pepperoni",
                                price = "$1.00",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "4",
                                title = "Olives",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "5",
                                title = "Onions",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "6",
                                title = "Bell Peppers",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "7",
                                title = "Bacon",
                                price = "$1.00",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "8",
                                title = "Spinach",
                                price = "$0.50",
                                imageUrl = ""
                            ),
                            MiniCardInfo(
                                id = "9",
                                title = "Pineapple",
                                price = "$1.00",
                                imageUrl = ""
                            )
                        ),
                        onToppingClick = { }
                    )
                }
                StickyBottomBar(
                    buttonText = "Add to Cart for \$12.99",
                    onButtonClick = {}
                )
            }
        }

    }
}


@Preview
@Composable
private fun ProductDetailScreenPreview() {
    LazyPizzaTheme {
        ProductDetailScreen(
            state = ProductDetailState(),
            onAction = {}
        )
    }
}