package com.danzucker.lazypizza.auth.presentation.auth.util


object PhoneValidator {

    /**
     * Validates international phone number format according to E.164 standard
     *
     * E.164 format rules:
     * - Starts with + followed by country code (1-3 digits)
     * - Total length: 7-15 digits (including country code)
     * - Examples:
     *   - US: +1234567890 (11 digits)
     *   - UK: +441234567890 (13 digits)
     *   - China: +861234567890 (13 digits)
     *   - Short: +12345678 (8 digits - some countries)
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val cleanNumber = phoneNumber.replace(Regex("[\\s()-]"), "")

        // Pattern breakdown:
        // ^\\+ - starts with +
        // [1-9] - first digit 1-9 (country code can't start with 0)
        // \\d{6,14} - followed by 6-14 more digits
        // Total: 7-15 digits (covers all E.164 valid numbers)
        return cleanNumber.matches(Regex("^\\+[1-9]\\d{6,14}$"))
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