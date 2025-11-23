package com.danzucker.lazypizza.product.presentation.mappers

import com.danzucker.lazypizza.product.domain.model.ToppingData
import com.danzucker.lazypizza.product.presentation.models.ToppingUi

fun ToppingUi.toToppingData(quantity: Int): ToppingData {
    return ToppingData(
        name = name,
        price = getPriceAsDouble(),
        quantity = quantity
    )
}