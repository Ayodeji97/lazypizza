package com.danzucker.lazypizza.product.domain.product

import com.danzucker.lazypizza.core.domain.util.DataError
import com.danzucker.lazypizza.core.domain.util.Result
import com.danzucker.lazypizza.product.domain.model.Product
import com.danzucker.lazypizza.product.domain.model.ProductCategory
import com.danzucker.lazypizza.product.domain.model.Topping
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Result<List<Product>, DataError.Network>>
    suspend fun getProductById(id: String): Result<Product?, DataError.Network>
    fun getToppings(): Flow<Result<List<Topping>, DataError.Network>>
    fun getProductsByCategory(category: ProductCategory): Flow<Result<List<Product>, DataError.Network>>
}