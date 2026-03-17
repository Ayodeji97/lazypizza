package com.danzucker.lazypizza.product.presentation.util

import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.datetime.Instant as KInstant

private val zone: ZoneId = ZoneId.systemDefault()

private fun locale(): Locale = Locale.getDefault()

private val orderFormatter by lazy {
    DateTimeFormatter.ofPattern("MMMM dd, HH:mm", locale())
}

private val timeOnlyFormatter by lazy {
    DateTimeFormatter.ofPattern("HH:mm", locale())
}

private val pickupFormatter by lazy {
    DateTimeFormatter.ofPattern("MMMM d, HH:mm", locale())
}

/**
 * Format order date from timestamp
 * Format: "December 08, 14:30"
 */
fun formatOrderDate(timestamp: Long): String {
    val zonedDateTime =
        KInstant
            .fromEpochMilliseconds(timestamp)
            .toJavaInstant()
            .atZone(zone)

    return zonedDateTime.format(orderFormatter)
}

/**
 * Format pickup time for display
 * If today, show only time: "12:15"
 * If future, show date + time: "SEPTEMBER 25, 12:15"
 */
fun formatPickupTime(timestamp: Long): String {
    val zonedDateTime =
        KInstant
            .fromEpochMilliseconds(timestamp)
            .toJavaInstant()
            .atZone(zone)

    val now = ZonedDateTime.now(zone)
    val isSameDay = zonedDateTime.toLocalDate() == now.toLocalDate()

    return if (isSameDay) {
        zonedDateTime.format(timeOnlyFormatter)
    } else {
        zonedDateTime.format(pickupFormatter).uppercase(locale())
    }
}
