package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimarySmallButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyPizzaTimePickerDialog(
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Get current time for initial state
    val now = Clock.System.now()
    val currentTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true  // Use 24-hour format
    )

    val isTimeOutsideRange by remember {
        derivedStateOf {
            val hour = timePickerState.hour
            val minute = timePickerState.minute

            // Outside 10:15 - 21:45?
            hour !in 10..21 ||
                    (hour == 21 && minute > 45) ||
                    (hour == 10 && minute < 15)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            PrimarySmallButton(
                text = stringResource(R.string.ok),
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                    onDismiss()
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.select_time_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Time Input
                TimeInput(
                    state = timePickerState
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Helper text from Figma
                if (isTimeOutsideRange) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.pickup_time_hours_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Valid Time - No Warning", showBackground = true)
@Composable
private fun TimePickerDialog_ValidTime_Preview1() {
    LazyPizzaTheme {
        LazyPizzaTimePickerDialog(
            onTimeSelected = { _, _ ->  // ✅ Fixed syntax
                // No-op for preview
            },
            onDismiss = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Valid Time - No Warning", showBackground = true)
@Composable
private fun TimePickerDialog_ValidTime_Preview() {
    LazyPizzaTheme {
        val timePickerState = rememberTimePickerState(
            initialHour = 12,  // Valid time
            initialMinute = 30,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                PrimarySmallButton(
                    text = "Ok",
                    onClick = {}
                )
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = {
                Text(
                    text = "SELECT TIME",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TimeInput(state = timePickerState)

                    // No warning shown (time is valid)

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Invalid Time - With Warning", showBackground = true)
@Composable
private fun TimePickerDialog_InvalidTime_Preview() {
    LazyPizzaTheme {
        val timePickerState = rememberTimePickerState(
            initialHour = 22,  // Invalid time (after 21:45)
            initialMinute = 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                PrimarySmallButton(
                    text = "Ok",
                    onClick = {}
                )
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = {
                Text(
                    text = "SELECT TIME",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TimeInput(state = timePickerState)

                    Spacer(modifier = Modifier.height(16.dp))

                    // ✅ Warning shown (time is invalid)
                    Text(
                        text = "Pickup available between 10:15 and 21:45",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Edge Case - 10:10 (Too Early)", showBackground = true)
@Composable
private fun TimePickerDialog_TooEarly_Preview() {
    LazyPizzaTheme {
        val timePickerState = rememberTimePickerState(
            initialHour = 10,  // Invalid (before 10:15)
            initialMinute = 10,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                PrimarySmallButton(
                    text = "Ok",
                    onClick = {}
                )
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = {
                Text(
                    text = "SELECT TIME",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TimeInput(state = timePickerState)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pickup available between 10:15 and 21:45",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Edge Case - 21:45 (Exactly Valid)", showBackground = true)
@Composable
private fun TimePickerDialog_ExactlyValid_Preview() {
    LazyPizzaTheme {
        val timePickerState = rememberTimePickerState(
            initialHour = 21,  // Valid (exactly 21:45)
            initialMinute = 45,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                PrimarySmallButton(
                    text = "Ok",
                    onClick = {}
                )
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = {
                Text(
                    text = "SELECT TIME",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TimeInput(state = timePickerState)

                    // No warning (21:45 is valid)

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )
    }
}