package com.danzucker.lazypizza.product.presentation.util


import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

object SampleProductProvider {

    fun getProducts(): List<LazyPizzaProductListUi> = listOf(
        // PIZZAS
        LazyPizzaProductListUi(
            id = "1",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            price = "$8.99",
            imageUrl = "",
            isAvailable = true,
            category = "Pizza",
            rating = 4.5f,
            reviewsCount = 150,
            isFavorite = false,
            cardType = LazyPizzaCardType.PIZZA
        ),
        LazyPizzaProductListUi(
            id = "2",
            name = "Pepperoni",
            description = "Tomato sauce, mozzarella, pepperoni",
            price = "$9.99",
            imageUrl = "",
            isAvailable = true,
            category = "Pizza",
            rating = 4.8f,
            reviewsCount = 200,
            isFavorite = true,
            cardType = LazyPizzaCardType.PIZZA
        ),
        LazyPizzaProductListUi(
            id = "3",
            name = "Hawaiian",
            description = "Tomato sauce, mozzarella, ham, pineapple",
            price = "$10.49",
            imageUrl = "",
            isAvailable = true,
            category = "Pizza",
            rating = 4.2f,
            reviewsCount = 120,
            isFavorite = false,
            cardType = LazyPizzaCardType.PIZZA
        ),
        LazyPizzaProductListUi(
            id = "4",
            name = "Veggie Delight",
            description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion",
            price = "$9.79",
            imageUrl = "",
            isAvailable = true,
            category = "Pizza",
            rating = 4.6f,
            reviewsCount = 180,
            isFavorite = false,
            cardType = LazyPizzaCardType.PIZZA
        ),

        // DRINKS
        LazyPizzaProductListUi(
            id = "5",
            name = "Mineral Water",
            description = "Refreshing mineral water",
            price = "$1.49",
            imageUrl = "",
            isAvailable = true,
            category = "Drinks",
            rating = 4.0f,
            reviewsCount = 50,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),
        LazyPizzaProductListUi(
            id = "6",
            name = "Pepsi",
            description = "Classic Pepsi cola",
            price = "$1.99",
            imageUrl = "",
            isAvailable = true,
            category = "Drinks",
            rating = 4.3f,
            reviewsCount = 80,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),
        LazyPizzaProductListUi(
            id = "7",
            name = "Orange Juice",
            description = "Fresh orange juice",
            price = "$2.49",
            imageUrl = "",
            isAvailable = true,
            category = "Drinks",
            rating = 4.5f,
            reviewsCount = 60,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),

        // SAUCES
        LazyPizzaProductListUi(
            id = "8",
            name = "Garlic Sauce",
            description = "Creamy garlic dipping sauce",
            price = "$0.59",
            imageUrl = "",
            isAvailable = true,
            category = "Sauces",
            rating = 4.7f,
            reviewsCount = 120,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),
        LazyPizzaProductListUi(
            id = "9",
            name = "BBQ Sauce",
            description = "Smoky BBQ dipping sauce",
            price = "$0.59",
            imageUrl = "",
            isAvailable = true,
            category = "Sauces",
            rating = 4.6f,
            reviewsCount = 100,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),

        // ICE CREAM
        LazyPizzaProductListUi(
            id = "10",
            name = "Vanilla Ice Cream",
            description = "Classic vanilla ice cream",
            price = "$3.99",
            imageUrl = "",
            isAvailable = true,
            category = "Ice Cream",
            rating = 4.8f,
            reviewsCount = 150,
            isFavorite = false,
            cardType = LazyPizzaCardType.OTHERS
        ),
        LazyPizzaProductListUi(
            id = "11",
            name = "Chocolate Ice Cream",
            description = "Rich chocolate ice cream",
            price = "$3.99",
            imageUrl = "",
            isAvailable = true,
            category = "Ice Cream",
            rating = 4.9f,
            reviewsCount = 180,
            isFavorite = true,
            cardType = LazyPizzaCardType.OTHERS
        )
    )

    fun getCategories(): List<String> = listOf("Pizza", "Drinks", "Sauces", "Ice Cream")
}