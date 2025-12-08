package com.danzucker.lazypizza.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.danzucker.lazypizza.core.data.FirestoreDataSeeder
import com.danzucker.lazypizza.core.data.preferences.DataStoreAppPreferencesStorage
import com.danzucker.lazypizza.core.data.preferences.appPreferencesDataStore
import com.danzucker.lazypizza.core.domain.preferences.AppPreferencesStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder(firestoreSettings)
                .setLocalCacheSettings(
                    PersistentCacheSettings.newBuilder()
                        .build()
                )
                .build()
        }
    }

    single<DataStore<Preferences>> {
        androidContext().appPreferencesDataStore
    }

    singleOf(::DataStoreAppPreferencesStorage) bind AppPreferencesStorage::class

    singleOf(::FirestoreDataSeeder)
}