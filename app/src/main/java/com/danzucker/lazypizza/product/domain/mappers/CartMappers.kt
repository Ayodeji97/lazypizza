package com.danzucker.lazypizza.product.domain.mappers

import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.presentation.cart.model.CartItemUi

fun CartItem.toCartItemUi(): CartItemUi {
    return CartItemUi(
        id = id,
        name = name,
        imageUrl = imageUrl,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        quantity = quantity,
        toppings = toppings.associate { it.name to it.quantity }
    )
}