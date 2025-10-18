package com.danzucker.lazypizza.product.presentation.productdetail

import com.danzucker.lazypizza.product.presentation.models.PizzaDetailUi
import com.danzucker.lazypizza.product.presentation.models.ToppingUi
import java.util.Locale

data class ProductDetailState(
    val isLoadingData: Boolean = false,
    val pizzaDetail: PizzaDetailUi? = null,
    val availableToppings: List<ToppingUi> = emptyList(),
    val selectedToppings: Map<String, Int> = emptyMap(), // toppingId -> quantity
    val basePizzaPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val errorMessage: String? = null
) {
    val formattedTotalPrice: String
        get() = String.format(
            Locale.getDefault(),
            "$%.2f", totalPrice
        )
}