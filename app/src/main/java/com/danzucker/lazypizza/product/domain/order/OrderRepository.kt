package com.danzucker.lazypizza.product.domain.order

import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.model.Order
import com.danzucker.lazypizza.product.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    /**
     * Create a new order
     * Saves to both /orders/{orderId} and /users/{userId}/orders/{orderId}
     */
    suspend fun createOrder(order: Order): Result<String, DataError.Network>

    fun getOrders(): Flow<Result<List<Order>, DataError.Network>>

    suspend fun getOrderById(orderId: String): Result<Order?, DataError.Network>

    suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus,
    ): EmptyResult<DataError.Network>

    suspend fun cancelOrder(orderId: String): EmptyResult<DataError.Network>
}
