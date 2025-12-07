package com.danzucker.lazypizza.auth.presentation.auth.util


object PhoneValidator {

    /**
     * Validates international phone number format
     * Expected format: +[country code][phone number]
     * Example: +1234567890
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val cleanNumber = phoneNumber.replace(Regex("[\\s()-]"), "")

        return cleanNumber.matches(Regex("^\\+[1-9]\\d{1,14}$"))
    }

    /**
     * Formats phone number as user types
     * Ensures it starts with + and contains only valid characters
     */
    fun formatPhoneNumber(input: String): String {
        // Remove all non-digit characters except +
        var formatted = input.filter { it.isDigit() || it == '+' }

        // Ensure it starts with +
        if (formatted.isEmpty()) {
            formatted = "+"
        } else if (!formatted.startsWith("+")) {
            formatted = "+$formatted"
        }

        // Remove duplicate + signs
        formatted = "+" + formatted.drop(1).replace("+", "")

        return formatted
    }
}