package com.danzucker.lazypizza.product.presentation.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.checkout.components.CommentsTextField
import com.danzucker.lazypizza.product.presentation.checkout.components.HorizontalPickupTimeOptions
import com.danzucker.lazypizza.product.presentation.checkout.components.OrderDetailsSection
import com.danzucker.lazypizza.product.presentation.checkout.components.OrderSummaryButton
import com.danzucker.lazypizza.product.presentation.components.RecommendedAddOnsSection
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

/**
 * Checkout landscape content with single-column scrollable layout
 *
 * Structure (same as portrait, just wider):
 * - Pickup time (horizontal buttons)
 * - Earliest pickup time
 * - Order details (collapsible with 2-column grid)
 * - Recommended add-ons (horizontal scroll)
 * - Comments
 * - Order Summary Button (sticky at bottom)
 *
 * Note: Using Column + verticalScroll instead of LazyColumn because OrderDetailsSection
 * contains regular Column (with grid layout), and we want to avoid any nested scrollables.
 */

@Composable
fun CheckoutLandscapeContent(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Single scrollable column with all content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pickup Time Section
            HorizontalPickupTimeOptions(
                selectedOption = state.pickupTimeOption,
                earliestPickupTime = state.earliestPickupTime,
                scheduledDateTime = state.scheduledDateTime,
                onOptionSelected = { option ->
                    onAction(CheckoutAction.OnPickupTimeSelected(option))
                }
            )

            // Order Details Section (Collapsible)
            OrderDetailsSection(
                items = state.orderItems,
                isExpanded = state.isOrderDetailsExpanded,
                onExpandToggle = { onAction(CheckoutAction.OnToggleOrderDetails) },
                onProductClick = { productId ->
                    onAction(CheckoutAction.OnProductClick(productId))
                },
                onQuantityChange = { productId, quantity ->
                    onAction(CheckoutAction.OnQuantityChange(productId, quantity))
                },
                onDeleteItem = { productId ->
                    onAction(CheckoutAction.OnDeleteItem(productId))
                },
                deviceScreenType = DeviceScreenType.TABLET_PORTRAIT
            )

            // Recommended Add-ons Section
            if (state.recommendedAddOns.isNotEmpty()) {
                RecommendedAddOnsSection(
                    recommendedAddOns = state.recommendedAddOns,
                    onAddClick = { addOnId ->
                        onAction(CheckoutAction.OnAddRecommendedItem(addOnId))
                    }
                )
            }

            // Comments Section
            CommentsTextField(
                value = state.comment,
                onValueChange = { comment ->
                    onAction(CheckoutAction.OnCommentChange(comment))
                }
            )
        }

        // Order Summary Button (sticky at bottom)
        OrderSummaryButton(
            totalAmount = state.totalAmount,
            onPlaceOrder = { onAction(CheckoutAction.OnPlaceOrder) },
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            isLoading = state.isPlacingOrder,
            enabled = state.canPlaceOrder
        )
    }
}

@Preview(widthDp = 840, showBackground = true)
@Composable
private fun CheckoutLandscapeContentPreview() {
    LazyPizzaTheme {
        CheckoutLandscapeContent(
            state = CheckoutState(
                pickupTimeOption = PickupTimeOption.EARLIEST,
                earliestPickupTime = "12:15",
                scheduledDateTime = null,
                orderItems = listOf(
                    LazyPizzaProductListUi(
                        id = "1",
                        name = "Margherita",
                        description = "Classic pizza",
                        price = "$8.99",
                        imageUrl = "",
                        isAvailable = true,
                        category = "Pizza",
                        rating = 4.5f,
                        reviewsCount = 150,
                        isFavorite = false,
                        cardType = LazyPizzaCardType.PIZZA,
                        quantityInCart = 2
                    ),
                    LazyPizzaProductListUi(
                        id = "2",
                        name = "Pepperoni",
                        description = "Spicy pizza",
                        price = "$9.99",
                        imageUrl = "",
                        isAvailable = true,
                        category = "Pizza",
                        rating = 4.7f,
                        reviewsCount = 180,
                        isFavorite = false,
                        cardType = LazyPizzaCardType.PIZZA,
                        quantityInCart = 2
                    ),
                    LazyPizzaProductListUi(
                        id = "3",
                        name = "Pepsi",
                        description = "Refreshing beverage",
                        price = "$1.99",
                        imageUrl = "",
                        isAvailable = true,
                        category = "Beverage",
                        rating = 4.0f,
                        reviewsCount = 50,
                        isFavorite = false,
                        cardType = LazyPizzaCardType.OTHERS,
                        quantityInCart = 2
                    ),
                    LazyPizzaProductListUi(
                        id = "4",
                        name = "Cookies Ice Cream",
                        description = "Delicious dessert",
                        price = "$1.49",
                        imageUrl = "",
                        isAvailable = true,
                        category = "Dessert",
                        rating = 4.8f,
                        reviewsCount = 200,
                        isFavorite = false,
                        cardType = LazyPizzaCardType.OTHERS,
                        quantityInCart = 1
                    )
                ),
                isOrderDetailsExpanded = true,
                recommendedAddOns = listOf(
                    RecommendedAddOnUi(
                        id = "r1",
                        name = "BBQ Sauce",
                        price = 0.59,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r2",
                        name = "Garlic Sauce",
                        price = 0.59,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r3",
                        name = "Vanilla Ice Cream",
                        price = 2.49,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r4",
                        name = "Orange Juice",
                        price = 2.49,
                        imageUrl = ""
                    ),
                    RecommendedAddOnUi(
                        id = "r5",
                        name = "Pistachio Ice Cream",
                        price = 2.99,
                        imageUrl = ""
                    )
                ),
                comment = "",
                totalAmount = 25.45
            ),
            onAction = {}
        )
    }
}