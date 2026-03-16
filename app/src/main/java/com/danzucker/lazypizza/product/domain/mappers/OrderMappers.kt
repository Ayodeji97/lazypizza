package com.danzucker.lazypizza.product.domain.mappers

import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.CartTopping
import com.danzucker.lazypizza.product.domain.model.OrderItem
import com.danzucker.lazypizza.product.domain.model.OrderTopping
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi

/**
 * Convert CartItem to OrderItem
 */
fun CartItem.toOrderItem(): OrderItem {
    return OrderItem(
        productId = productId,
        productName = name,
        quantity = quantity,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        imageUrl = imageUrl,
        toppings = toppings.map { it.toOrderTopping() },
        category = category
    )
}

/**
 * Convert CartTopping to OrderTopping
 */
fun CartTopping.toOrderTopping(): OrderTopping {
    return OrderTopping(
        id = id,
        name = name,
        price = price,
        quantity = quantity
    )
}

