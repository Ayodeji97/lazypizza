@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.product.presentation.orderconfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.SecondaryButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.BackButton
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaBackground
import com.danzucker.lazypizza.core.presentation.designsystem.components.LazyPizzaCenteredTopAppBar
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.applyIf
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.lazypizza.product.presentation.orderconfirmation.components.OrderInfoCard


@Composable
fun OrderConfirmationScreen(
    orderId: String,
    orderNumber: String,
    pickupTime: String,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->

        val windowClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceScreenType = fromWindowSizeClass(windowSizeClass = windowClass)
        val isMobilePortrait = deviceScreenType == MOBILE_PORTRAIT

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyPizzaBackground(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .shadow(
                        elevation = 8.dp,
                        spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                    ),
                topStartCornerRadius = 16.dp,
                topEndCornerRadius = 16.dp,
                bottomStartCornerRadius = 0.dp,
                bottomEndCornerRadius = 0.dp,
                topPadding = 0.dp,
                bottomPadding = 0.dp,
                horizontalStartPadding = 0.dp,
                horizontalEndPadding = 0.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    LazyPizzaCenteredTopAppBar(
                        title = stringResource(R.string.order_checkout_title),
                        titleColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIcon = {
                            BackButton(
                                onClick = onBackToMenu,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.5f))
                    OrderConfirmationContent(
                        orderNumber = orderNumber,
                        pickupTime = pickupTime,
                        onBackToMenu = onBackToMenu,
                        modifier = Modifier
                            .applyIf(!isMobilePortrait) {
                                fillMaxWidth(0.5f)
                            }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun OrderConfirmationContent(
    orderNumber: String,
    pickupTime: String,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.order_placed_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.order_placed_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surfaceTint
        )

        Spacer(modifier = Modifier.height(16.dp))

        OrderInfoCard(
            orderNumber = orderNumber,
            pickupTime = pickupTime
        )

        SecondaryButton(
            text = stringResource(R.string.back_to_menu_button),
            onClick = onBackToMenu
        )
    }
}



@Preview
@Composable
private fun OrderConfirmationContentPreview() {
    LazyPizzaTheme {
        OrderConfirmationScreen(
            orderId = "#1",
            orderNumber = "#12345",
            pickupTime = "September 25, 12:15",
            onBackToMenu = {},
        )
    }
}

@Preview
@Composable
private fun OrderConfirmationScreenPreview() {
    LazyPizzaTheme {
        OrderConfirmationContent(
            orderNumber = "#12345",
            pickupTime = "September 25, 12:15",
            onBackToMenu = {}
        )
    }
}