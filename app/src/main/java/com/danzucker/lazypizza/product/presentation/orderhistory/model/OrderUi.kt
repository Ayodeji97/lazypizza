package com.danzucker.lazypizza.product.presentation.orderhistory.model

import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.util.UiText

data class OrderUi(
    val id: String,
    val orderNumber: String,
    val date: String, // formatted date
    val items: List<OrderItemUi>,
    val totalAmount: String, // formatted price
    val status: OrderStatusUi
)

data class OrderItemUi(
    val productName: String,
    val quantity: Int
) {
    val displayText: String
        get() = "$quantity x $productName"
}

enum class OrderStatusUi(val displayName: UiText) {
    IN_PROGRESS(UiText.StringResourceWithArgs(R.string.in_progress_order)),
    COMPLETED(UiText.StringResourceWithArgs(R.string.completed)),
    CANCELLED(UiText.StringResourceWithArgs(R.string.cancelled))
}