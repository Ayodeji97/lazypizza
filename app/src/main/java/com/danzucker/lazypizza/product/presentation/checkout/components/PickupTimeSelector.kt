package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        PickupTimeOptionCard(
            isSelected = selectedOption == PickupTimeOption.EARLIEST,
            title = stringResource(R.string.earliest_available_time),
            subtitle = null,
            onClick = { onOptionSelected(PickupTimeOption.EARLIEST) }
        )

        // Schedule time option
        PickupTimeOptionCard(
            isSelected = selectedOption == PickupTimeOption.SCHEDULED,
            title = stringResource(R.string.schedule_time),
            subtitle = scheduledDateTime,
            onClick = { onOptionSelected(PickupTimeOption.SCHEDULED) }
        )

        // Show earliest pickup time when EARLIEST is selected
        if (selectedOption == PickupTimeOption.EARLIEST) {
            Text(
                text = stringResource(R.string.earliest_pickup_time_label, earliestPickupTime),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
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
            PickupTimeOptionCard(
                isSelected = selectedOption == PickupTimeOption.EARLIEST,
                title = stringResource(R.string.earliest_available_time),
                subtitle = null,
                onClick = { onOptionSelected(PickupTimeOption.EARLIEST) },
                modifier = Modifier.weight(1f)
            )

            // Schedule time option
            PickupTimeOptionCard(
                isSelected = selectedOption == PickupTimeOption.SCHEDULED,
                title = stringResource(R.string.schedule_time),
                subtitle = scheduledDateTime,
                onClick = { onOptionSelected(PickupTimeOption.SCHEDULED) },
                modifier = Modifier.weight(1f)
            )
        }

        // Show earliest pickup time when EARLIEST is selected
        if (selectedOption == PickupTimeOption.EARLIEST) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.earliest_pickup_time_label, earliestPickupTime),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun PickupTimeOptionCard(
    isSelected: Boolean,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
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