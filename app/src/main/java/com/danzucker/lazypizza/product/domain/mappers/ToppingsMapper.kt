package com.danzucker.lazypizza.product.domain.mappers

import com.danzucker.lazypizza.product.domain.model.CartTopping
import com.danzucker.lazypizza.product.domain.model.ToppingData

fun ToppingData.toCartTopping(id: String): CartTopping {
    return CartTopping(
        id = id,
        name = name,
        price = price,
        quantity = quantity
    )
}