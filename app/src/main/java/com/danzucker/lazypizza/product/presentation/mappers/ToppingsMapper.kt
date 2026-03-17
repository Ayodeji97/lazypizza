package com.danzucker.lazypizza.product.presentation.mappers

import com.danzucker.lazypizza.product.domain.model.ToppingData
import com.danzucker.lazypizza.product.presentation.models.ToppingUi

fun ToppingUi.toToppingData(quantity: Int): ToppingData =
    ToppingData(
        name = name,
        price = getPriceAsDouble(),
        quantity = quantity,
    )
