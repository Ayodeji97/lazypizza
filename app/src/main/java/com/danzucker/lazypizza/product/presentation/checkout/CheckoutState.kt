package com.danzucker.lazypizza.product.presentation.checkout

import com.danzucker.lazypizza.product.presentation.cart.model.RecommendedAddOnUi
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import kotlin.collections.isNotEmpty

data class CheckoutState(
    val pickupTimeOption: PickupTimeOption = PickupTimeOption.EARLIEST,
    val earliestPickupTime: String = "",
    val scheduledDateTime: String? = null,
    val orderItems: List<LazyPizzaProductListUi> = emptyList(),
    val isOrderDetailsExpanded: Boolean = false,
    val recommendedAddOns: List<RecommendedAddOnUi> = emptyList(),
    val comment: String = "",
    val totalAmount: Double = 0.0,
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null
) {
    val canPlaceOrder: Boolean
        get() = orderItems.isNotEmpty() && !isPlacingOrder
}


enum class PickupTimeOption {
    EARLIEST,
    SCHEDULED
}