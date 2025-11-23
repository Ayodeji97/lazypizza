package com.danzucker.lazypizza.product.presentation.util

import com.danzucker.lazypizza.core.domain.ImageUrlProvider
import com.danzucker.lazypizza.product.domain.SampleProductRepository
import com.danzucker.lazypizza.product.presentation.mappers.toProductListUi
import com.danzucker.lazypizza.product.presentation.mappers.toToppingUi
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import com.danzucker.lazypizza.product.presentation.models.ToppingUi

object SampleProductProvider {

    /**
     * Get all products as UI models
     *
     * Note: In production, quantityInCart would come from CartRepository
     * For now, it defaults to 0 and is managed in ViewModels
     */
    fun getProducts(): List<LazyPizzaProductListUi> {
        return SampleProductRepository.getProducts().map { product ->
            product.toProductListUi(quantityInCart = 0)
        }
    }

    /**
     * Get all product categories
     */
    fun getCategories(): List<String> {
        return SampleProductRepository.getCategories()
    }

    /**
     * Get all toppings as UI models
     */
    fun getToppings(): List<ToppingUi> {
        return SampleProductRepository.getToppings().map { topping ->
            topping.toToppingUi()
        }
    }

    /**
     * Get product by ID as UI model
     */
    fun getProductById(id: String): LazyPizzaProductListUi? {
        return SampleProductRepository.getProductById(id)?.toProductListUi()
    }

    /**
     * Get topping by ID as UI model
     */
    fun getToppingById(id: String): ToppingUi? {
        return SampleProductRepository.getToppingById(id)?.toToppingUi()
    }

//    fun getProducts(): List<LazyPizzaProductListUi> = listOf(
//        // ============ PIZZAS ============
//        LazyPizzaProductListUi(
//            id = "1",
//            name = "Margherita",
//            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
//            price = "$8.99",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Margherita.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.5f,
//            reviewsCount = 150,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "2",
//            name = "Pepperoni",
//            description = "Tomato sauce, mozzarella, pepperoni",
//            price = "$9.99",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Pepperoni.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.8f,
//            reviewsCount = 200,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "3",
//            name = "Hawaiian",
//            description = "Tomato sauce, mozzarella, ham, pineapple",
//            price = "$10.49",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Hawaiian.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.2f,
//            reviewsCount = 120,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "4",
//            name = "BBQ Chicken",
//            description = "BBQ sauce, mozzarella, grilled chicken, onion, corn",
//            price = "$11.49",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("BBQ Chicken.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.7f,
//            reviewsCount = 190,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "5",
//            name = "Four Cheese",
//            description = "Mozzarella, gorgonzola, parmesan, ricotta",
//            price = "$11.99",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Four Cheese.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.6f,
//            reviewsCount = 165,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "6",
//            name = "Veggie Delight",
//            description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",
//            price = "$9.79",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Veggie Delight.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.6f,
//            reviewsCount = 180,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "7",
//            name = "Meat Lovers",
//            description = "Tomato sauce, mozzarella, pepperoni, ham, bacon, sausage",
//            price = "$12.49",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Meat Lovers.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.9f,
//            reviewsCount = 250,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "8",
//            name = "Spicy Inferno",
//            description = "Tomato sauce, mozzarella, spicy salami, jalapeños, red chili pepper, garlic",
//            price = "$11.29",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Spicy Inferno.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.4f,
//            reviewsCount = 140,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "9",
//            name = "Seafood Special",
//            description = "Tomato sauce, mozzarella, shrimp, mussels, squid, parsley",
//            price = "$13.99",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Seafood Special.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.3f,
//            reviewsCount = 110,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//        LazyPizzaProductListUi(
//            id = "10",
//            name = "Truffle Mushroom",
//            description = "Cream sauce, mozzarella, mushrooms, truffle oil, parmesan",
//            price = "$12.99",
//            imageUrl = ImageUrlProvider.getPizzaImageUrl("Truffle Mushroom.png"),
//            isAvailable = true,
//            category = "Pizza",
//            rating = 4.8f,
//            reviewsCount = 175,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.PIZZA
//        ),
//
//        // ============ DRINKS ============
//        LazyPizzaProductListUi(
//            id = "11",
//            name = "Mineral Water",
//            description = "Refreshing mineral water",
//            price = "$1.49",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("mineral water.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.0f,
//            reviewsCount = 50,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "12",
//            name = "7-Up",
//            description = "Lemon-lime soda",
//            price = "$1.89",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("7-up.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.2f,
//            reviewsCount = 75,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "13",
//            name = "Pepsi",
//            description = "Classic Pepsi cola",
//            price = "$1.99",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("pepsi.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.3f,
//            reviewsCount = 80,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "14",
//            name = "Orange Juice",
//            description = "Fresh orange juice",
//            price = "$2.49",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("orange juice.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.5f,
//            reviewsCount = 60,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "15",
//            name = "Apple Juice",
//            description = "Fresh apple juice",
//            price = "$2.29",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("apple juice.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.4f,
//            reviewsCount = 55,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "16",
//            name = "Iced Tea (Lemon)",
//            description = "Refreshing lemon iced tea",
//            price = "$2.19",
//            imageUrl = ImageUrlProvider.getDrinkImageUrl("iced tea.png"),
//            isAvailable = true,
//            category = "Drinks",
//            rating = 4.3f,
//            reviewsCount = 65,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//
//        // ============ SAUCES ============
//        LazyPizzaProductListUi(
//            id = "17",
//            name = "Garlic Sauce",
//            description = "Creamy garlic dipping sauce",
//            price = "$0.59",
//            imageUrl = ImageUrlProvider.getSauceImageUrl("Garlic Sauce.png"),
//            isAvailable = true,
//            category = "Sauces",
//            rating = 4.7f,
//            reviewsCount = 120,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "18",
//            name = "BBQ Sauce",
//            description = "Smoky BBQ dipping sauce",
//            price = "$0.59",
//            imageUrl = ImageUrlProvider.getSauceImageUrl("BBQ Sauce.png"),
//            isAvailable = true,
//            category = "Sauces",
//            rating = 4.6f,
//            reviewsCount = 100,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "19",
//            name = "Cheese Sauce",
//            description = "Rich and creamy cheese sauce",
//            price = "$0.89",
//            imageUrl = ImageUrlProvider.getSauceImageUrl("Cheese Sauce.png"),
//            isAvailable = true,
//            category = "Sauces",
//            rating = 4.8f,
//            reviewsCount = 130,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "20",
//            name = "Spicy Chili Sauce",
//            description = "Hot and spicy chili sauce",
//            price = "$0.59",
//            imageUrl = ImageUrlProvider.getSauceImageUrl("Spicy Chili Sauce.png"),
//            isAvailable = true,
//            category = "Sauces",
//            rating = 4.5f,
//            reviewsCount = 95,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//
//        // ============ ICE CREAM ============
//        LazyPizzaProductListUi(
//            id = "21",
//            name = "Vanilla Ice Cream",
//            description = "Classic vanilla ice cream",
//            price = "$2.49",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("vanilla.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.8f,
//            reviewsCount = 150,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "22",
//            name = "Chocolate Ice Cream",
//            description = "Rich chocolate ice cream",
//            price = "$2.49",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("chocolate.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.9f,
//            reviewsCount = 180,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "23",
//            name = "Strawberry Ice Cream",
//            description = "Sweet strawberry ice cream",
//            price = "$2.49",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("strawberry.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.7f,
//            reviewsCount = 140,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "24",
//            name = "Cookies Ice Cream",
//            description = "Vanilla ice cream with cookie chunks",
//            price = "$2.79",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("cookies.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.8f,
//            reviewsCount = 160,
//            isFavorite = true,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "25",
//            name = "Pistachio Ice Cream",
//            description = "Creamy pistachio ice cream",
//            price = "$2.99",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("pistachio.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.6f,
//            reviewsCount = 125,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        ),
//        LazyPizzaProductListUi(
//            id = "26",
//            name = "Mango Sorbet",
//            description = "Refreshing mango sorbet",
//            price = "$2.69",
//            imageUrl = ImageUrlProvider.getIceCreamImageUrl("mango sorbet.png"),
//            isAvailable = true,
//            category = "Ice Cream",
//            rating = 4.7f,
//            reviewsCount = 135,
//            isFavorite = false,
//            cardType = LazyPizzaCardType.OTHERS
//        )
//    )
//
//    fun getCategories(): List<String> = listOf("Pizza", "Drinks", "Sauces", "Ice Cream")
//
//    fun getToppings(): List<ToppingUi> = listOf(
//        ToppingUi(
//            id = "1",
//            name = "Bacon",
//            price = "$1.00",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("bacon.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "2",
//            name = "Extra Cheese",
//            price = "$1.00",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("cheese.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "3",
//            name = "Corn",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("corn.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "4",
//            name = "Tomato",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("tomato.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "5",
//            name = "Olives",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("olive.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "6",
//            name = "Pepperoni",
//            price = "$1.00",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("pepperoni.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "7",
//            name = "Mushrooms",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("mashroom.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "8",
//            name = "Basil",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("basil.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "9",
//            name = "Pineapple",
//            price = "$1.00",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("pineapple.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "10",
//            name = "Onion",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("onion.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "11",
//            name = "Chili Peppers",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("chilli.png"),
//            maxQuantity = 3
//        ),
//        ToppingUi(
//            id = "12",
//            name = "Spinach",
//            price = "$0.50",
//            imageUrl = ImageUrlProvider.getToppingImageUrl("spinach.png"),
//            maxQuantity = 3
//        )
//    )
}