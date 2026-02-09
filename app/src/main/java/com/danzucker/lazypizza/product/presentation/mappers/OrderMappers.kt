package com.danzucker.lazypizza.product.presentation.mappers

import com.danzucker.lazypizza.product.domain.model.Order
import com.danzucker.lazypizza.product.domain.model.OrderItem
import com.danzucker.lazypizza.product.domain.model.OrderStatus
import com.danzucker.lazypizza.product.presentation.models.LazyPizzaProductListUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderItemUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderStatusUi
import com.danzucker.lazypizza.product.presentation.orderhistory.model.OrderUi
import com.danzucker.lazypizza.product.presentation.util.formatAmount
import com.danzucker.lazypizza.product.presentation.util.formatOrderDate

/**
 * Convert LazyPizzaProductListUi to OrderItem (for recommended add-ons)
 */
fun LazyPizzaProductListUi.toOrderItem(): OrderItem {
    val priceDouble = price.removePrefix("$").toDoubleOrNull() ?: 0.0
    return OrderItem(
        productId = id,
        productName = name,
        quantity = quantityInCart,
        unitPrice = priceDouble,
        totalPrice = priceDouble * quantityInCart,
        imageUrl = imageUrl,
        toppings = emptyList(),
        category = category
    )
}

/**
 * Convert Order domain model to OrderUi presentation model
 */
fun Order.toOrderUi(): OrderUi {
    return OrderUi(
        id = id,
        orderNumber = orderNumber,
        date = formatOrderDate(createdAt),
        items = items.map { it.toOrderItemUi() },
        totalAmount = formatAmount(total),
        status = status.toOrderStatusUi()
    )
}

/**
 * Convert OrderItem to OrderItemUi
 */
fun OrderItem.toOrderItemUi(): OrderItemUi {
    return OrderItemUi(
        quantity = quantity,
        productName = productName
    )
}

/**
 * Convert OrderStatus to OrderStatusUi
 */
fun OrderStatus.toOrderStatusUi(): OrderStatusUi {
    return when (this) {
        OrderStatus.IN_PROGRESS -> OrderStatusUi.IN_PROGRESS
        OrderStatus.COMPLETED -> OrderStatusUi.COMPLETED
        OrderStatus.CANCELLED -> OrderStatusUi.CANCELLED
    }
}