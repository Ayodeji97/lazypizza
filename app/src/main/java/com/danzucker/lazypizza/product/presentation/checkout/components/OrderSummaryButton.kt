package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimaryButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.util.formatAmount

@Composable
fun OrderSummaryButton(
    totalAmount: Double,
    onPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier,
    deviceScreenType: DeviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val isMobilePortrait = deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 0f,
                    endY = 200f
                )
            )
            .padding(16.dp)
    ) {
        if (isMobilePortrait) {
            OrderSummaryMobileLayout(
                totalAmount = totalAmount,
                onPlaceOrder = onPlaceOrder,
                isLoading = isLoading,
                enabled = enabled
            )
        } else {
            OrderSummaryWideLayout(
                totalAmount = totalAmount,
                onPlaceOrder = onPlaceOrder,
                isLoading = isLoading,
                enabled = enabled
            )
        }
    }
}


@Composable
private fun OrderSummaryMobileLayout(
    totalAmount: Double,
    onPlaceOrder: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.order_total_label),
                style = MaterialTheme.typography.displaySmall.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = formatAmount(totalAmount),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        PrimaryButton(
            text = stringResource(R.string.place_order_button),
            onClick = onPlaceOrder,
            isLoading = isLoading,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OrderSummaryWideLayout(
    totalAmount: Double,
    onPlaceOrder: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.order_total_label),
                style = MaterialTheme.typography.displaySmall.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = formatAmount(totalAmount),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        PrimaryButton(
            text = stringResource(R.string.place_order_button),
            onClick = onPlaceOrder,
            isLoading = isLoading,
            enabled = enabled,
            modifier = Modifier.width(200.dp)
        )
    }
}


@Preview(name = "Mobile Portrait")
@Composable
private fun OrderSummaryButtonMobilePreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(200.dp))
                OrderSummaryButton(
                    totalAmount = 25.45,
                    onPlaceOrder = {},
                    deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT
                )
            }
        }
    }
}

@Preview(name = "Mobile Portrait - Loading")
@Composable
private fun OrderSummaryButtonMobileLoadingPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(200.dp))
                OrderSummaryButton(
                    totalAmount = 25.45,
                    onPlaceOrder = {},
                    deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
                    isLoading = true
                )
            }
        }
    }
}

@Preview(name = "Tablet/Wide", widthDp = 840)
@Composable
private fun OrderSummaryButtonWidePreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(200.dp))
                OrderSummaryButton(
                    totalAmount = 25.45,
                    onPlaceOrder = {},
                    deviceScreenType = DeviceScreenType.TABLET_PORTRAIT
                )
            }
        }
    }
}

@Preview(name = "Tablet/Wide - Loading", widthDp = 840)
@Composable
private fun OrderSummaryButtonWideLoadingPreview() {
    LazyPizzaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(200.dp))
                OrderSummaryButton(
                    totalAmount = 25.45,
                    onPlaceOrder = {},
                    deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
                    isLoading = true
                )
            }
        }
    }
}