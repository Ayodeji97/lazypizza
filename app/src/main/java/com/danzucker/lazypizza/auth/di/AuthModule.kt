package com.danzucker.lazypizza.auth.di

import com.danzucker.lazypizza.auth.data.AuthManager
import com.danzucker.lazypizza.auth.data.AuthRepositoryImpl
import com.danzucker.lazypizza.auth.domain.AuthRepository
import com.danzucker.lazypizza.auth.presentation.auth.AuthViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthManager)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    viewModelOf(::AuthViewModel)
}