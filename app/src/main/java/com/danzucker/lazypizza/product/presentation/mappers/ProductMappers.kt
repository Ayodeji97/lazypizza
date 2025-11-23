package com.danzucker.lazypizza.product.presentation.mappers

import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.domain.model.ProductType
import com.danzucker.lazypizza.product.domain.model.Topping
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaCardType
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import com.danzucker.lazypizza.product.presentation.models.ToppingUi
import com.danzucker.lazypizza.product.presentation.util.formatAmount

fun Product.toProductListUi(quantityInCart: Int = 0): LazyPizzaProductListUi {
    return LazyPizzaProductListUi(
        id = id,
        name = name,
        description = description,
        price = formatAmount(price),
        imageUrl = imageUrl,
        isAvailable = isAvailable,
        category = category.displayName,
        rating = rating,
        reviewsCount = reviewsCount,
        isFavorite = false, // TODO: Get from favorites repository
        cardType = type.toCardType(),
        quantityInCart = quantityInCart
    )
}

fun Topping.toToppingUi(): ToppingUi {
    return ToppingUi(
        id = id,
        name = name,
        price = formatAmount(price),
        imageUrl = imageUrl,
        maxQuantity = maxQuantity
    )
}

fun LazyPizzaProductListUi.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = parsePrice(price),
        imageUrl = imageUrl,
        category = ProductCategory.fromString(category),
        isAvailable = isAvailable,
        rating = rating,
        reviewsCount = reviewsCount,
        type = cardType.toProductType()
    )
}

private fun ProductType.toCardType(): LazyPizzaCardType {
    return when (this) {
        ProductType.PIZZA -> LazyPizzaCardType.PIZZA
        ProductType.OTHER -> LazyPizzaCardType.OTHERS
    }
}

private fun LazyPizzaCardType.toProductType(): ProductType {
    return when (this) {
        LazyPizzaCardType.PIZZA -> ProductType.PIZZA
        LazyPizzaCardType.OTHERS -> ProductType.OTHER
    }
}

private fun parsePrice(priceString: String): Double {
    return priceString.removePrefix("$").toDoubleOrNull() ?: 0.0
}

/**
 * Extension to get price as Double from LazyPizzaProductListUi
 */
fun LazyPizzaProductListUi.getPriceAsDouble(): Double {
    return parsePrice(price)
}

/**
 * Extension to get price as Double from ToppingUi
 */
fun ToppingUi.getPriceAsDouble(): Double {
    return parsePrice(price)
}