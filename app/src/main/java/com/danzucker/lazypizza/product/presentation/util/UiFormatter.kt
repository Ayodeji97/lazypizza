package com.danzucker.lazypizza.product.presentation.util

import java.util.Locale

/**
 * Formats toppings map into display string
 * Example: {("Extra Cheese" to 1), ("Olives" to 2)} -> "1×Extra Cheese, 2×Olives"
 */
fun formatToppings(toppings: Map<String, Int>): String {
    return toppings.entries
        .sortedBy { it.key }
        .joinToString(", ") { (topping, quantity) ->
            "${quantity}×${topping}"
        }
}

fun formatAmount(vararg amount: Double): String {
    return String.format(Locale.getDefault(), "$%.2f", amount)
}

fun formatTime(minutes: Int, seconds: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}