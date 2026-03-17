package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimarySmallButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaPrimaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaSurfaceHighestColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTextPrimaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTextSecondaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

@Composable
fun LazyPizzaTimePickerDialog(
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    var hourText by remember { mutableStateOf(String.format(Locale.getDefault(), "%02d", now.hour)) }
    var minuteText by remember { mutableStateOf(String.format(Locale.getDefault(), "%02d", now.minute)) }

    val hour by remember { derivedStateOf { hourText.toIntOrNull() ?: 0 } }
    val minute by remember { derivedStateOf { minuteText.toIntOrNull() ?: 0 } }

    val isTimeOutsideRange by remember {
        derivedStateOf {
            hour !in 10..21 || (hour == 21 && minute > 45) || (hour == 10 && minute < 15)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier =
                    Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
            ) {
                val hPadding = 24.dp
                Column(
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                ) {
                    // "SELECT TIME" label
                    Text(
                        text = stringResource(R.string.select_time_title),
                        modifier = Modifier.padding(horizontal = hPadding),
                        style = MaterialTheme.typography.labelSmall,
                        color = LazyPizzaTextSecondaryColor,
                        letterSpacing = 1.sp,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hour : Minute — fields fill the dialog width equally
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = hPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TimeFieldColumn(
                            value = hourText,
                            label = stringResource(R.string.hour),
                            onValueChange = { new ->
                                if (new.length <= 2 && new.all { it.isDigit() }) hourText = new
                            },
                            modifier = Modifier.weight(1f),
                        )

                        // Colon separator
                        Text(
                            text = ":",
                            modifier =
                                Modifier
                                    .width(28.dp)
                                    .padding(bottom = 20.dp),
                            textAlign = TextAlign.Center,
                            style =
                                MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                            color = LazyPizzaTextPrimaryColor,
                        )

                        TimeFieldColumn(
                            value = minuteText,
                            label = stringResource(R.string.minute),
                            onValueChange = { new ->
                                if (new.length <= 2 && new.all { it.isDigit() }) minuteText = new
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    // Warning — only shown when time is outside operating hours
                    if (isTimeOutsideRange) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.pickup_time_hours_hint),
                            modifier =
                                Modifier
                                    .padding(horizontal = hPadding)
                                    .fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall,
                            color = LazyPizzaPrimaryColor,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = LazyPizzaTextSecondaryColor.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cancel / Ok buttons
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = hPadding),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = LazyPizzaPrimaryColor,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        PrimarySmallButton(
                            text = stringResource(R.string.ok),
                            onClick = {
                                onTimeSelected(hour, minute)
                                onDismiss()
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A time input field (hour or minute) with a label underneath.
 * Fills its parent width so both fields share the available space equally.
 */
@Composable
private fun TimeFieldColumn(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(LazyPizzaPrimaryColor),
            textStyle =
                MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = LazyPizzaTextPrimaryColor,
                    textAlign = TextAlign.Center,
                ),
            decorationBox = { innerTextField ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clip(shape)
                            .background(
                                if (isFocused) Color.White else LazyPizzaSurfaceHighestColor,
                            ).then(
                                if (isFocused) {
                                    Modifier.border(1.dp, LazyPizzaPrimaryColor, shape)
                                } else {
                                    Modifier
                                },
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    innerTextField()
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = LazyPizzaTextSecondaryColor,
        )
    }
}

@Preview(name = "Time Picker – valid", showBackground = true)
@Composable
private fun TimePickerDialog_Valid_Preview() {
    LazyPizzaTheme {
        LazyPizzaTimePickerDialog(onTimeSelected = { _, _ -> }, onDismiss = {})
    }
}

@Preview(name = "Time Picker – out of range", showBackground = true)
@Composable
private fun TimePickerDialog_OutOfRange_Preview() {
    LazyPizzaTheme {
        LazyPizzaTimePickerDialog(onTimeSelected = { _, _ -> }, onDismiss = {})
    }
}
