package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.checkout.PickupTimeOption

@Composable
fun VerticalPickupTimeOptions(
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
        Text(
            text = stringResource(R.string.pickup_time_subtitle),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

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

        // Show confirmed scheduled time, or fall back to earliest available time
        val displayTime = scheduledDateTime ?: earliestPickupTime
        if (displayTime.isNotEmpty()) {
            PickupTime(time = displayTime)
        }
    }
}


@Preview(name = "Phone - Vertical Layout")
@Composable
private fun PickupTimeSelectorPhonePreview() {
    LazyPizzaTheme {
        VerticalPickupTimeOptions(
            selectedOption = PickupTimeOption.EARLIEST,
            earliestPickupTime = "12:15",
            scheduledDateTime = null,
            onOptionSelected = {}
        )
    }
}

@Preview(name = "Phone - Scheduled", showBackground = true)
@Composable
private fun PickupTimeSelectorPhoneScheduledPreview() {
    LazyPizzaTheme {
        VerticalPickupTimeOptions(
            selectedOption = PickupTimeOption.SCHEDULED,
            earliestPickupTime = "12:15",
            scheduledDateTime = "November 25, 18:30",
            onOptionSelected = {}
        )
    }
}