package com.danzucker.lazypizza.product.presentation.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.checkout.PickupTimeOption
import com.danzucker.lazypizza.product.presentation.checkout.components.CommentsTextField
import com.danzucker.lazypizza.product.presentation.checkout.components.OrderDetailsSection
import com.danzucker.lazypizza.product.presentation.checkout.components.OrderSummaryButton
import com.danzucker.lazypizza.product.presentation.checkout.components.PickupTimeSelector
import com.danzucker.lazypizza.product.presentation.components.RecommendedAddOnsSection
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

/**
 * Checkout landscape content with two-column layout
 *
 * Structure:
 * - Left column: Pickup time + Order details (scrollable)
 * - Right column: Recommended add-ons + Comments + Order Summary Button
 *
 * Note: Using Column + verticalScroll instead of LazyColumn because OrderDetailsSection
 * contains LazyVerticalGrid, and nested vertical scrollables are not allowed.
 */
@Composable
fun CheckoutLandscapeContent(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left column: Pickup time + Order details (scrollable)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pickup Time Section
            PickupTimeSelector(
                selectedOption = state.pickupTimeOption,
                earliestPickupTime = state.earliestPickupTime,
                scheduledDateTime = state.scheduledDateTime,
                deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
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
        }

        // Right column: Recommended add-ons + Comments + Order Summary
        LazyPizzaBackground(
            bottomStartCornerRadius = 16.dp,
            modifier = Modifier.weight(1f)
        ) {
            // Recommended Add-ons Section
            if (state.recommendedAddOns.isNotEmpty()) {
                RecommendedAddOnsSection(
                    recommendedAddOns = state.recommendedAddOns,
                    onAddClick = { addOnId ->
                        onAction(CheckoutAction.OnAddRecommendedItem(addOnId))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Comments Section
            CommentsTextField(
                value = state.comment,
                onValueChange = { comment ->
                    onAction(CheckoutAction.OnCommentChange(comment))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order Summary Button
            OrderSummaryButton(
                totalAmount = state.totalAmount,
                onPlaceOrder = { onAction(CheckoutAction.OnPlaceOrder) },
                deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
                isLoading = state.isPlacingOrder,
                enabled = state.canPlaceOrder
            )
        }
    }
}

@Preview(widthDp = 840, showBackground = true)
@Composable
private fun CheckoutLandscapeContentPreview() {
    LazyPizzaTheme {
        CheckoutLandscapeContent(
            state = CheckoutState(
                pickupTimeOption = PickupTimeOption.SCHEDULED,
                earliestPickupTime = "12:15",
                scheduledDateTime = "November 25, 18:30",
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
                    )
                ),
                isOrderDetailsExpanded = false,
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