package com.danzucker.lazypizza.product.domain

import kotlin.collections.filter
import kotlin.collections.find
import com.danzucker.lazypizza.core.domain.ImageUrlProvider
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.domain.model.ProductType
import com.danzucker.lazypizza.product.domain.model.Topping

/**
 * Sample product repository - provides mock data
 * In production, this would be replaced with a real repository
 * that fetches data from Firebase or a REST API
 */
object SampleProductRepository {

    /**
     * Get all products from the catalog
     */
    fun getProducts(): List<Product> = listOf(
        // ============ PIZZAS ============
        Product(
            id = "1",
            name = "Margherita",
            description = "Tomato sauce, mozzarella, fresh basil, olive oil",
            price = 8.99,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Margherita.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.5f,
            reviewsCount = 150,
            type = ProductType.PIZZA
        ),
        Product(
            id = "2",
            name = "Pepperoni",
            description = "Tomato sauce, mozzarella, pepperoni",
            price = 9.99,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Pepperoni.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.8f,
            reviewsCount = 200,
            type = ProductType.PIZZA
        ),
        Product(
            id = "3",
            name = "Hawaiian",
            description = "Tomato sauce, mozzarella, ham, pineapple",
            price = 10.49,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Hawaiian.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.2f,
            reviewsCount = 120,
            type = ProductType.PIZZA
        ),
        Product(
            id = "4",
            name = "BBQ Chicken",
            description = "BBQ sauce, mozzarella, grilled chicken, onion, corn",
            price = 11.49,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("BBQ Chicken.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.7f,
            reviewsCount = 190,
            type = ProductType.PIZZA
        ),
        Product(
            id = "5",
            name = "Four Cheese",
            description = "Mozzarella, gorgonzola, parmesan, ricotta",
            price = 11.99,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Four Cheese.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.6f,
            reviewsCount = 165,
            type = ProductType.PIZZA
        ),
        Product(
            id = "6",
            name = "Veggie Delight",
            description = "Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",
            price = 9.79,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Veggie Delight.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.6f,
            reviewsCount = 180,
            type = ProductType.PIZZA
        ),
        Product(
            id = "7",
            name = "Meat Lovers",
            description = "Tomato sauce, mozzarella, pepperoni, ham, bacon, sausage",
            price = 12.49,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Meat Lovers.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.9f,
            reviewsCount = 250,
            type = ProductType.PIZZA
        ),
        Product(
            id = "8",
            name = "Spicy Inferno",
            description = "Tomato sauce, mozzarella, spicy salami, jalapeños, red chili pepper, garlic",
            price = 11.29,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Spicy Inferno.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.4f,
            reviewsCount = 140,
            type = ProductType.PIZZA
        ),
        Product(
            id = "9",
            name = "Seafood Special",
            description = "Tomato sauce, mozzarella, shrimp, mussels, squid, parsley",
            price = 13.99,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Seafood Special.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.3f,
            reviewsCount = 110,
            type = ProductType.PIZZA
        ),
        Product(
            id = "10",
            name = "Truffle Mushroom",
            description = "Cream sauce, mozzarella, mushrooms, truffle oil, parmesan",
            price = 12.99,
            imageUrl = ImageUrlProvider.getPizzaImageUrl("Truffle Mushroom.png"),
            category = ProductCategory.PIZZA,
            isAvailable = true,
            rating = 4.8f,
            reviewsCount = 175,
            type = ProductType.PIZZA
        ),

        // ============ DRINKS ============
        Product(
            id = "11",
            name = "Mineral Water",
            description = "Refreshing mineral water",
            price = 1.49,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("mineral water.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.0f,
            reviewsCount = 50,
            type = ProductType.OTHER
        ),
        Product(
            id = "12",
            name = "7-Up",
            description = "Lemon-lime soda",
            price = 1.89,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("7-up.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.2f,
            reviewsCount = 75,
            type = ProductType.OTHER
        ),
        Product(
            id = "13",
            name = "Pepsi",
            description = "Classic Pepsi cola",
            price = 1.99,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("pepsi.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.3f,
            reviewsCount = 80,
            type = ProductType.OTHER
        ),
        Product(
            id = "14",
            name = "Orange Juice",
            description = "Fresh orange juice",
            price = 2.49,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("orange juice.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.5f,
            reviewsCount = 60,
            type = ProductType.OTHER
        ),
        Product(
            id = "15",
            name = "Apple Juice",
            description = "Fresh apple juice",
            price = 2.29,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("apple juice.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.4f,
            reviewsCount = 55,
            type = ProductType.OTHER
        ),
        Product(
            id = "16",
            name = "Iced Tea (Lemon)",
            description = "Refreshing lemon iced tea",
            price = 2.19,
            imageUrl = ImageUrlProvider.getDrinkImageUrl("iced tea.png"),
            category = ProductCategory.DRINKS,
            isAvailable = true,
            rating = 4.3f,
            reviewsCount = 65,
            type = ProductType.OTHER
        ),

        // ============ SAUCES ============
        Product(
            id = "17",
            name = "Garlic Sauce",
            description = "Creamy garlic dipping sauce",
            price = 0.59,
            imageUrl = ImageUrlProvider.getSauceImageUrl("Garlic Sauce.png"),
            category = ProductCategory.SAUCES,
            isAvailable = true,
            rating = 4.7f,
            reviewsCount = 120,
            type = ProductType.OTHER
        ),
        Product(
            id = "18",
            name = "BBQ Sauce",
            description = "Smoky BBQ dipping sauce",
            price = 0.59,
            imageUrl = ImageUrlProvider.getSauceImageUrl("BBQ Sauce.png"),
            category = ProductCategory.SAUCES,
            isAvailable = true,
            rating = 4.6f,
            reviewsCount = 100,
            type = ProductType.OTHER
        ),
        Product(
            id = "19",
            name = "Cheese Sauce",
            description = "Rich and creamy cheese sauce",
            price = 0.89,
            imageUrl = ImageUrlProvider.getSauceImageUrl("Cheese Sauce.png"),
            category = ProductCategory.SAUCES,
            isAvailable = true,
            rating = 4.8f,
            reviewsCount = 130,
            type = ProductType.OTHER
        ),
        Product(
            id = "20",
            name = "Spicy Chili Sauce",
            description = "Hot and spicy chili sauce",
            price = 0.59,
            imageUrl = ImageUrlProvider.getSauceImageUrl("Spicy Chili Sauce.png"),
            category = ProductCategory.SAUCES,
            isAvailable = true,
            rating = 4.5f,
            reviewsCount = 95,
            type = ProductType.OTHER
        ),

        // ============ ICE CREAM ============
        Product(
            id = "21",
            name = "Vanilla Ice Cream",
            description = "Classic vanilla ice cream",
            price = 2.49,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("vanilla.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.8f,
            reviewsCount = 150,
            type = ProductType.OTHER
        ),
        Product(
            id = "22",
            name = "Chocolate Ice Cream",
            description = "Rich chocolate ice cream",
            price = 2.49,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("chocolate.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.9f,
            reviewsCount = 180,
            type = ProductType.OTHER
        ),
        Product(
            id = "23",
            name = "Strawberry Ice Cream",
            description = "Sweet strawberry ice cream",
            price = 2.49,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("strawberry.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.7f,
            reviewsCount = 140,
            type = ProductType.OTHER
        ),
        Product(
            id = "24",
            name = "Cookies Ice Cream",
            description = "Vanilla ice cream with cookie chunks",
            price = 2.79,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("cookies.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.8f,
            reviewsCount = 160,
            type = ProductType.OTHER
        ),
        Product(
            id = "25",
            name = "Pistachio Ice Cream",
            description = "Creamy pistachio ice cream",
            price = 2.99,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("pistachio.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.6f,
            reviewsCount = 125,
            type = ProductType.OTHER
        ),
        Product(
            id = "26",
            name = "Mango Sorbet",
            description = "Refreshing mango sorbet",
            price = 2.69,
            imageUrl = ImageUrlProvider.getIceCreamImageUrl("mango sorbet.png"),
            category = ProductCategory.ICE_CREAM,
            isAvailable = true,
            rating = 4.7f,
            reviewsCount = 135,
            type = ProductType.OTHER
        )
    )

    /**
     * Get all available product categories
     */
    fun getCategories(): List<String> {
        return ProductCategory.entries.map { it.displayName }
    }

    /**
     * Get all available toppings
     */
    fun getToppings(): List<Topping> = listOf(
        Topping(
            id = "t1",
            name = "Bacon",
            price = 1.00,
            imageUrl = ImageUrlProvider.getToppingImageUrl("bacon.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t2",
            name = "Extra Cheese",
            price = 1.00,
            imageUrl = ImageUrlProvider.getToppingImageUrl("cheese.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t3",
            name = "Corn",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("corn.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t4",
            name = "Tomato",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("tomato.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t5",
            name = "Olives",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("olive.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t6",
            name = "Pepperoni",
            price = 1.00,
            imageUrl = ImageUrlProvider.getToppingImageUrl("pepperoni.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t7",
            name = "Mushrooms",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("mashroom.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t8",
            name = "Basil",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("basil.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t9",
            name = "Pineapple",
            price = 1.00,
            imageUrl = ImageUrlProvider.getToppingImageUrl("pineapple.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t10",
            name = "Onion",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("onion.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t11",
            name = "Chili Peppers",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("chilli.png"),
            maxQuantity = 3,
            isAvailable = true
        ),
        Topping(
            id = "t12",
            name = "Spinach",
            price = 0.50,
            imageUrl = ImageUrlProvider.getToppingImageUrl("spinach.png"),
            maxQuantity = 3,
            isAvailable = true
        )
    )

    /**
     * Get product by ID
     */
    fun getProductById(id: String): Product? {
        return getProducts().find { it.id == id }
    }

    /**
     * Get topping by ID
     */
    fun getToppingById(id: String): Topping? {
        return getToppings().find { it.id == id }
    }

    /**
     * Get products by category
     */
    fun getProductsByCategory(category: ProductCategory): List<Product> {
        return getProducts().filter { it.category == category }
    }
}