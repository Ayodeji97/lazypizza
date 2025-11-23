package com.danzucker.lazypizza.app.di

import com.danzucker.lazypizza.app.LazyPizzaApplication
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as LazyPizzaApplication).applicationScope
    }
}