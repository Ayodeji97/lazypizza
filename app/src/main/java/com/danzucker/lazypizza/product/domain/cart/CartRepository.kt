package com.danzucker.lazypizza.product.domain.cart

import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.CartSummary
import com.danzucker.lazypizza.core.domain.util.DataError
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun getCartSummary(): Flow<CartSummary>
    suspend fun addToCart(item: CartItem): EmptyResult<DataError>
    suspend fun updateQuantity(itemId: String, quantity: Int): EmptyResult<DataError>
    suspend fun removeFromCart(itemId: String): EmptyResult<DataError>
    suspend fun clearCart(): EmptyResult<DataError>
    fun getCartItemsCount(): Flow<Int>
}