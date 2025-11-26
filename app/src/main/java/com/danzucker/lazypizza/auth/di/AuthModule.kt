package com.danzucker.lazypizza.auth.di

import com.danzucker.lazypizza.auth.data.AuthManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthManager)
}