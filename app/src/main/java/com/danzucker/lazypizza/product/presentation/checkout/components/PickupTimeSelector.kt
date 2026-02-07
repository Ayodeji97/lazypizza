package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.lazypizza.product.presentation.checkout.PickupTimeOption

@Composable
fun PickupTimeSelector(
    selectedOption: PickupTimeOption,
    earliestPickupTime: String,
    scheduledDateTime: String?,
    deviceScreenType: DeviceScreenType,
    onOptionSelected: (PickupTimeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Section Title
        Text(
            text = stringResource(R.string.pickup_time_subtitle),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Responsive layout based on device screen type
        val isMobilePortrait = deviceScreenType == DeviceScreenType.MOBILE_PORTRAIT

        if (isMobilePortrait) {
            // Vertical layout for mobile portrait
            VerticalPickupTimeOptions(
                selectedOption = selectedOption,
                earliestPickupTime = earliestPickupTime,
                scheduledDateTime = scheduledDateTime,
                onOptionSelected = onOptionSelected
            )
        } else {
            // Horizontal layout for tablets and larger screens
            HorizontalPickupTimeOptions(
                selectedOption = selectedOption,
                earliestPickupTime = earliestPickupTime,
                scheduledDateTime = scheduledDateTime,
                onOptionSelected = onOptionSelected
            )
        }
    }
}

@Composable
private fun VerticalPickupTimeOptions(
    selectedOption: PickupTimeOption,
    earliestPickupTime: String,
    scheduledDateTime: String?,
    onOptionSelected: (PickupTimeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Earliest available time option
        PickupTimeRadioButton(
            text = stringResource(R.string.earliest_available_time),
            selected = selectedOption == PickupTimeOption.EARLIEST,
            onClick = { onOptionSelected(PickupTimeOption.EARLIEST) }
        )

        // Schedule time option
        PickupTimeRadioButton(
            text = stringResource(R.string.schedule_time),
            selected = selectedOption == PickupTimeOption.SCHEDULED,
            onClick = { onOptionSelected(PickupTimeOption.SCHEDULED) }
        )

        // Show earliest pickup time when EARLIEST is selected
        if (earliestPickupTime != "") {
            EarliestPickupTime(
                earliestPickupTime = earliestPickupTime
            )
        }
    }
}

@Composable
private fun HorizontalPickupTimeOptions(
    selectedOption: PickupTimeOption,
    earliestPickupTime: String,
    scheduledDateTime: String?,
    onOptionSelected: (PickupTimeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Earliest available time option

            PickupTimeRadioButton(
                text = stringResource(R.string.earliest_available_time),
                selected = selectedOption == PickupTimeOption.EARLIEST,
                onClick = { onOptionSelected(PickupTimeOption.EARLIEST) },
                modifier = Modifier.weight(1f)
            )

            PickupTimeRadioButton(
                text = stringResource(R.string.schedule_time),
                selected = selectedOption == PickupTimeOption.SCHEDULED,
                onClick = { onOptionSelected(PickupTimeOption.SCHEDULED) },
                modifier = Modifier.weight(1f)
            )
        }

        // Show earliest pickup time when EARLIEST is selected
        if (scheduledDateTime != "") {
            Spacer(modifier = Modifier.height(12.dp))
            EarliestPickupTime(
                earliestPickupTime = scheduledDateTime ?: ""
            )
        }
    }
}


@Composable
fun EarliestPickupTime(
    earliestPickupTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.earliest_pickup_time_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )

        Text(
            text = earliestPickupTime,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(name = "Phone - Vertical Layout")
@Composable
private fun PickupTimeSelectorPhonePreview() {
    LazyPizzaTheme {
        PickupTimeSelector(
            selectedOption = PickupTimeOption.EARLIEST,
            earliestPickupTime = "12:15",
            scheduledDateTime = null,
            deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
            onOptionSelected = {}
        )
    }
}

@Preview(name = "Phone - Scheduled", showBackground = true)
@Composable
private fun PickupTimeSelectorPhoneScheduledPreview() {
    LazyPizzaTheme {
        PickupTimeSelector(
            selectedOption = PickupTimeOption.SCHEDULED,
            earliestPickupTime = "12:15",
            scheduledDateTime = "November 25, 18:30",
            deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
            onOptionSelected = {}
        )
    }
}

@Preview(name = "Tablet - Horizontal Layout", widthDp = 840, showBackground = true)
@Composable
private fun PickupTimeSelectorTabletPreview() {
    LazyPizzaTheme {
        PickupTimeSelector(
            selectedOption = PickupTimeOption.EARLIEST,
            earliestPickupTime = "12:15",
            scheduledDateTime = null,
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            onOptionSelected = {}
        )
    }
}

@Preview(name = "Tablet - Scheduled", widthDp = 840, showBackground = true)
@Composable
private fun PickupTimeSelectorTabletScheduledPreview() {
    LazyPizzaTheme {
        PickupTimeSelector(
            selectedOption = PickupTimeOption.SCHEDULED,
            earliestPickupTime = "12:15",
            scheduledDateTime = "November 25, 18:30",
            deviceScreenType = DeviceScreenType.TABLET_PORTRAIT,
            onOptionSelected = {}
        )
    }
}