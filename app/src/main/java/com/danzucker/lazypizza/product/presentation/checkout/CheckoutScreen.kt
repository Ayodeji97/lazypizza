@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.components.BackButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaCenteredTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.ObserveAsEvents
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.checkout.components.LazyPizzaDatePickerDialog
import com.danzucker.lazypizza.product.presentation.checkout.components.LazyPizzaTimePickerDialog
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckoutRoot(
    onNavigateBack: () -> Unit,
    onNavigateToOrderConfirmation: (orderId: String, orderNumber: String, pickupTime: String) -> Unit,
    viewModel: CheckoutViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CheckoutEvent.NavigateBack -> onNavigateBack()

            is CheckoutEvent.NavigateToOrderConfirmation -> {
                onNavigateToOrderConfirmation(
                    event.orderId,
                    event.orderNumber,
                    event.pickupTime,
                )
            }

            CheckoutEvent.ShowDatePicker -> {
                showDatePicker = true
            }

            CheckoutEvent.ShowTimePicker -> {
                showTimePicker = true
            }

            is CheckoutEvent.ShowError -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
            }

            is CheckoutEvent.ShowMessage -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        LazyPizzaDatePickerDialog(
            onDateSelected = { dateMillis ->
                viewModel.onDateSelected(dateMillis)
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
                viewModel.onPickerDismissed()
            },
        )
    }

    // Time Picker Dialog
    if (showTimePicker) {
        LazyPizzaTimePickerDialog(
            onTimeSelected = { hour, minute ->
                viewModel.onTimeSelected(hour, minute)
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
                viewModel.onPickerDismissed()
            },
        )
    }

    CheckoutScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
        ) {
            LazyPizzaBackground(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .shadow(
                            elevation = 8.dp,
                            spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        ),
                topStartCornerRadius = 16.dp,
                topEndCornerRadius = 16.dp,
                bottomStartCornerRadius = 0.dp,
                bottomEndCornerRadius = 0.dp,
                topPadding = 0.dp,
                bottomPadding = 0.dp,
                horizontalStartPadding = 0.dp,
                horizontalEndPadding = 0.dp,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LazyPizzaCenteredTopAppBar(
                        title = stringResource(R.string.order_checkout_title),
                        titleColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIcon = {
                            BackButton(
                                onClick = {
                                    onAction(CheckoutAction.OnBackPressed)
                                },
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        },
                    )

                    if (deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT) {
                        CheckoutPortraitContent(
                            state = state,
                            onAction = onAction,
                            modifier = modifier,
                        )
                    } else {
                        CheckoutLandscapeContent(
                            state = state,
                            onAction = onAction,
                            modifier = modifier,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Mobile Portrait", showBackground = true)
@Composable
fun CheckoutScreenPortraitPreview() {
    LazyPizzaTheme {
        CheckoutScreen(
            state =
                CheckoutState(
                    pickupTimeOption = PickupTimeOption.EARLIEST,
                    earliestPickupTime = "12:15",
                    scheduledDateTime = null,
                    orderItems =
                        listOf(
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
                                quantityInCart = 2,
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
                                quantityInCart = 2,
                            ),
                        ),
                    isOrderDetailsExpanded = true,
                    recommendedAddOns =
                        listOf(
                            RecommendedAddOnUi(
                                id = "r1",
                                name = "BBQ Sauce",
                                price = 0.59,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r2",
                                name = "Garlic Sauce",
                                price = 0.59,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r3",
                                name = "Vanilla Ice Cream",
                                price = 2.49,
                                imageUrl = "",
                            ),
                        ),
                    comment = "",
                    totalAmount = 25.45,
                ),
            onAction = {},
        )
    }
}

@Preview(name = "Tablet/Wide (840+)", widthDp = 840, showBackground = true)
@Composable
fun CheckoutScreenLandscapePreview() {
    LazyPizzaTheme {
        CheckoutScreen(
            state =
                CheckoutState(
                    pickupTimeOption = PickupTimeOption.SCHEDULED,
                    earliestPickupTime = "12:15",
                    scheduledDateTime = "November 25, 18:30",
                    orderItems =
                        listOf(
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
                                quantityInCart = 2,
                            ),
                        ),
                    isOrderDetailsExpanded = false,
                    recommendedAddOns =
                        listOf(
                            RecommendedAddOnUi(
                                id = "r1",
                                name = "BBQ Sauce",
                                price = 0.59,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r2",
                                name = "Garlic Sauce",
                                price = 0.59,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r3",
                                name = "Vanilla Ice Cream",
                                price = 2.49,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r4",
                                name = "Orange Juice",
                                price = 2.49,
                                imageUrl = "",
                            ),
                            RecommendedAddOnUi(
                                id = "r5",
                                name = "Pistachio Ice Cream",
                                price = 2.99,
                                imageUrl = "",
                            ),
                        ),
                    comment = "Please add extra napkins",
                    totalAmount = 25.45,
                ),
            onAction = {},
        )
    }
}
