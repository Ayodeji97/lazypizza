package com.danzucker.lazypizza.app

import android.app.Application
import com.danzucker.lazypizza.BuildConfig
import com.danzucker.lazypizza.app.di.appModule
import com.danzucker.lazypizza.auth.di.authModule
import com.danzucker.lazypizza.core.di.coreModule
import com.danzucker.lazypizza.product.di.productModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class LazyPizzaApplication : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or components here if needed
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@LazyPizzaApplication)
            modules(appModule)
            modules(productModule)
            modules(authModule)
            modules(coreModule)
        }
    }
}