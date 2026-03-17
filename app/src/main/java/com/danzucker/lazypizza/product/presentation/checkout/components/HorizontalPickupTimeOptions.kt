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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.product.presentation.checkout.PickupTimeOption

@Composable
fun HorizontalPickupTimeOptions(
    selectedOption: PickupTimeOption,
    earliestPickupTime: String,
    scheduledDateTime: String?,
    onOptionSelected: (PickupTimeOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.pickup_time_subtitle),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Earliest available time option

            PickupTimeRadioButton(
                text = stringResource(R.string.earliest_available_time),
                selected = selectedOption == PickupTimeOption.EARLIEST,
                onClick = { onOptionSelected(PickupTimeOption.EARLIEST) },
                modifier = Modifier.weight(1f),
            )

            PickupTimeRadioButton(
                text = stringResource(R.string.schedule_time),
                selected = selectedOption == PickupTimeOption.SCHEDULED,
                onClick = { onOptionSelected(PickupTimeOption.SCHEDULED) },
                modifier = Modifier.weight(1f),
            )
        }

        // Show confirmed scheduled time, or fall back to earliest available time
        val displayTime = scheduledDateTime ?: earliestPickupTime
        if (displayTime.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            PickupTime(time = displayTime)
        }
    }
}

@Preview(name = "Tablet - Horizontal Layout", widthDp = 840, showBackground = true)
@Composable
private fun PickupTimeSelectorTabletPreview() {
    LazyPizzaTheme {
        HorizontalPickupTimeOptions(
            selectedOption = PickupTimeOption.EARLIEST,
            earliestPickupTime = "12:15",
            scheduledDateTime = null,
            onOptionSelected = {},
        )
    }
}

@Preview(name = "Tablet - Scheduled", widthDp = 840, showBackground = true)
@Composable
private fun PickupTimeSelectorTabletScheduledPreview() {
    LazyPizzaTheme {
        HorizontalPickupTimeOptions(
            selectedOption = PickupTimeOption.SCHEDULED,
            earliestPickupTime = "12:15",
            scheduledDateTime = "November 25, 18:30",
            onOptionSelected = {},
        )
    }
}
