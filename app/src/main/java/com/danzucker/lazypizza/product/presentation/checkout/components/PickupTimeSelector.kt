package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.checkout.PickupTimeOption

@Composable
fun PickupTimeSelector(
    selectedOption: PickupTimeOption,
    earliestPickupTime: String,
    scheduledDateTime: String?,
    deviceScreenType: DeviceScreenType,
    onOptionSelected: (PickupTimeOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        // Section Title
        Text(
            text = stringResource(R.string.pickup_time_subtitle),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Responsive layout based on device screen type
        val isMobilePortrait = deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT

        if (isMobilePortrait) {
            // Vertical layout for mobile portrait
            VerticalPickupTimeOptions(
                selectedOption = selectedOption,
                earliestPickupTime = earliestPickupTime,
                scheduledDateTime = scheduledDateTime,
                onOptionSelected = onOptionSelected,
            )
        } else {
            // Horizontal layout for tablets and larger screens
            HorizontalPickupTimeOptions(
                selectedOption = selectedOption,
                earliestPickupTime = earliestPickupTime,
                scheduledDateTime = scheduledDateTime,
                onOptionSelected = onOptionSelected,
            )
        }
    }
}
