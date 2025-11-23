package com.danzucker.lazypizza.product.data

import com.danzucker.lazypizza.core.domain.util.EmptyResult
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.domain.model.CartItem
import com.danzucker.lazypizza.product.domain.model.CartSummary
import com.danzucker.notemark.core.domain.util.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory implementation of CartRepository
 *
 * This stores cart data in memory and will be replaced with
 * Firebase implementation in the next milestone.
 *
 * Usage: Inject as singleton in your DI setup
 */
class InMemoryCartRepository : CartRepository {
    private val cartItems = MutableStateFlow<Map<String, CartItem>>(emptyMap())

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartItems.map { itemsMap ->
            itemsMap.values.sortedByDescending { it.timestamp }
        }
    }

    override fun getCartSummary(): Flow<CartSummary> {
        return cartItems.map { itemsMap ->
            CartSummary(items = itemsMap.values.toList())
        }
    }

    override suspend fun addToCart(item: CartItem): EmptyResult<DataError> {
        val currentItems = cartItems.value.toMutableMap()

        // Check if item already exists
        val existingItem = currentItems[item.id]

        if (existingItem != null) {
            // Update quantity
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + item.quantity
            )
            currentItems[item.id] = updatedItem
        } else {
            // Add new item
            currentItems[item.id] = item
        }

        cartItems.value = currentItems
        return Result.Success(Unit)
    }

    override suspend fun updateQuantity(
        itemId: String,
        quantity: Int
    ): EmptyResult<DataError> {
        val currentItems = cartItems.value.toMutableMap()
        val item = currentItems[itemId] ?: return Result.Error(
            DataError.Network.UNKNOWN
        )

        if (quantity <= 0) {
            // Remove item if quantity is 0 or negative
            currentItems.remove(itemId)
        } else {
            // Update quantity
            currentItems[itemId] = item.copy(quantity = quantity)
        }

        cartItems.value = currentItems
        return Result.Success(Unit)
    }

    override suspend fun removeFromCart(itemId: String): EmptyResult<DataError> {
        val currentItems = cartItems.value.toMutableMap()
        currentItems.remove(itemId)
        cartItems.value = currentItems
        return Result.Success(Unit)
    }

    override suspend fun clearCart(): EmptyResult<DataError> {
        cartItems.value = emptyMap()
        return Result.Success(Unit)
    }

    override fun getCartItemsCount(): Flow<Int> {
        return cartItems.map { itemsMap ->
            itemsMap.values.sumOf { it.quantity }
        }
    }
}