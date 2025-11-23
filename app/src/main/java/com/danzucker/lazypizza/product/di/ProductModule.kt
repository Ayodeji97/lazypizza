package com.danzucker.lazypizza.product.di

import com.danzucker.lazypizza.product.data.InMemoryCartRepository
import com.danzucker.lazypizza.product.domain.cart.CartRepository
import com.danzucker.lazypizza.product.presentation.cart.CartViewModel
import com.danzucker.lazypizza.product.presentation.productdetail.ProductDetailViewModel
import com.danzucker.lazypizza.product.presentation.productlist.ProductListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productModule = module {
    singleOf(::InMemoryCartRepository).bind<CartRepository>()
    //viewModel { ProductListViewModel(get()) }
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailViewModel)
    viewModelOf(::CartViewModel)
}