package com.danzucker.lazypizza.core.domain.preferences

interface AppPreferencesStorage {
    suspend fun hasSeededData(): Boolean
    suspend fun setHasSeededData(value: Boolean)
}