package com.danzucker.lazypizza.core.domain

object ImageUrlProvider {

    private const val BASE_URL = "https://pl-coding.com/wp-content/uploads/lazypizza"
    // Category folders matching your file structure
    private const val DRINK_FOLDER = "drink"
    private const val ICE_CREAM_FOLDER = "ice cream"
    private const val PIZZA_FOLDER = "pizza"
    private const val SAUCE_FOLDER = "sauce"
    private const val TOPPINGS_FOLDER = "toppings"

    fun getPizzaImageUrl(filename: String): String {
        return "$BASE_URL/$PIZZA_FOLDER/$filename"
    }

    fun getDrinkImageUrl(filename: String): String {
        return "$BASE_URL/$DRINK_FOLDER/$filename"
    }

    fun getIceCreamImageUrl(filename: String): String {
        return "$BASE_URL/$ICE_CREAM_FOLDER/$filename"
    }

    fun getSauceImageUrl(filename: String): String {
        return "$BASE_URL/$SAUCE_FOLDER/$filename"
    }

    fun getToppingImageUrl(filename: String): String {
        return "$BASE_URL/$TOPPINGS_FOLDER/$filename"
    }

    // Generic method based on category
    fun getImageUrl(category: String, filename: String): String {
        val folder = when (category.lowercase()) {
            "pizza" -> PIZZA_FOLDER
            "drinks" -> DRINK_FOLDER
            "ice cream" -> ICE_CREAM_FOLDER
            "sauces" -> SAUCE_FOLDER
            else -> PIZZA_FOLDER
        }
        return "$BASE_URL/$folder/$filename"
    }
}