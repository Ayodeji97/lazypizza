package com.danzucker.lazypizza.product.presentation.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.danzucker.lazypizza.R
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import com.danzucker.lazypizza.core.presentation.designsystem.button.PrimarySmallButton
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaButtonGradient
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaPrimaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTextPrimaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTextSecondaryColor
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun LazyPizzaDatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val locale = Locale.getDefault()
    val localeFirstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
    val dayHeaders = remember(locale) {
        (0..6).map { offset ->
            val dow = DayOfWeek.of((localeFirstDayOfWeek.value - 1 + offset) % 7 + 1)
            dow.getDisplayName(TextStyle.NARROW, locale)
        }
    }

    var selectedDate by remember { mutableStateOf(today) }
    // Track the first day of the currently displayed month for navigation
    var displayFirstDay by remember { mutableStateOf(LocalDate(today.year, today.month, 1)) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Box provides the horizontal margin and centers the Surface on large screens
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp
        ) {
            val hPadding = 24.dp
            Column(
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            ) {
                // "SELECT DATE" label
                Text(
                    text = stringResource(R.string.select_date_title),
                    modifier = Modifier.padding(horizontal = hPadding),
                    style = MaterialTheme.typography.labelSmall,
                    color = LazyPizzaTextSecondaryColor,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Large selected date heading, e.g. "September 25"
                val selectedMonthName = selectedDate.month.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() }
                Text(
                    text = "$selectedMonthName ${selectedDate.dayOfMonth}",
                    modifier = Modifier.padding(horizontal = hPadding),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LazyPizzaTextPrimaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = LazyPizzaTextSecondaryColor.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(16.dp))

                // Month/year header with prev/next arrows
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val displayMonthName = displayFirstDay.month.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                    Text(
                        text = "$displayMonthName ${displayFirstDay.year}",
                        style = MaterialTheme.typography.labelMedium,
                        color = LazyPizzaTextSecondaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.previous_month_description),
                        tint = LazyPizzaTextPrimaryColor,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                displayFirstDay = displayFirstDay.minus(1, DateTimeUnit.MONTH)
                            }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next_month_description),
                        tint = LazyPizzaTextPrimaryColor,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                displayFirstDay = displayFirstDay.plus(1, DateTimeUnit.MONTH)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Day-of-week headers ordered by locale's first day of week
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding)
                ) {
                    dayHeaders.forEach { header ->
                        Text(
                            text = header,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = LazyPizzaTextSecondaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar grid
                buildCalendarDays(displayFirstDay, localeFirstDayOfWeek).chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = hPadding, vertical = 1.dp)
                    ) {
                        week.forEach { date ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (date != null) {
                                    val isSelected = date == selectedDate
                                    val isToday = date == today
                                    val isPast = date < today

                                    val cellModifier = Modifier
                                        .size(36.dp)
                                        .then(
                                            when {
                                                isSelected -> Modifier
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.LazyPizzaButtonGradient)
                                                isToday -> Modifier
                                                    .clip(CircleShape)
                                                    .border(1.dp, LazyPizzaPrimaryColor, CircleShape)
                                                else -> Modifier
                                            }
                                        )
                                        .then(
                                            if (!isPast) Modifier.clickable { selectedDate = date }
                                            else Modifier
                                        )

                                    Box(
                                        modifier = cellModifier,
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = date.dayOfMonth.toString(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = when {
                                                isSelected -> Color.White
                                                isToday -> LazyPizzaPrimaryColor
                                                isPast -> LazyPizzaTextSecondaryColor.copy(alpha = 0.4f)
                                                else -> LazyPizzaTextPrimaryColor
                                            },
                                            fontWeight = if (isSelected || isToday) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(color = LazyPizzaTextSecondaryColor.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel / Ok buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = hPadding),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = LazyPizzaPrimaryColor,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    PrimarySmallButton(
                        text = stringResource(R.string.ok),
                        onClick = {
                            val millis = selectedDate
                                .atStartOfDayIn(TimeZone.UTC)
                                .toEpochMilliseconds()
                            onDateSelected(millis)
                            onDismiss()
                        }
                    )
                }
            }
        }
        } // Box
    }
}

/**
 * Builds a flat list of [LocalDate?] for a 7-column calendar grid.
 * The grid starts on [firstDayOfWeek] (locale-aware).
 * Leading and trailing nulls pad incomplete weeks.
 */
private fun buildCalendarDays(
    firstDayOfMonth: LocalDate,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
): List<LocalDate?> {
    // Both kotlinx.datetime DayOfWeek.value and java.time.DayOfWeek.value use ISO (Monday=1…Sunday=7).
    // startOffset: number of blank columns before the 1st of the month.
    val startOffset = ((firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value) + 7) % 7
    val lastDay = firstDayOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

    val days = mutableListOf<LocalDate?>()
    repeat(startOffset) { days.add(null) }
    for (day in 1..lastDay.dayOfMonth) {
        days.add(LocalDate(firstDayOfMonth.year, firstDayOfMonth.month, day))
    }
    val remainder = days.size % 7
    if (remainder != 0) repeat(7 - remainder) { days.add(null) }
    return days
}

@Preview(name = "Date Picker Dialog", showBackground = true)
@Composable
private fun LazyPizzaDatePickerDialog_Preview() {
    LazyPizzaTheme {
        LazyPizzaDatePickerDialog(
            onDateSelected = {},
            onDismiss = {}
        )
    }
}