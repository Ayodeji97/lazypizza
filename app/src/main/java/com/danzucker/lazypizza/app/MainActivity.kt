package com.danzucker.lazypizza.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.danzucker.lazypizza.BuildConfig
import androidx.lifecycle.lifecycleScope
import com.danzucker.lazypizza.core.domain.util.Result
import androidx.navigation.compose.rememberNavController
import com.danzucker.lazypizza.app.navigation.NavigationRoot
import com.danzucker.lazypizza.core.data.FirestoreDataSeeder
import com.danzucker.lazypizza.core.domain.preferences.AppPreferencesStorage
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val appPreferences: AppPreferencesStorage by inject()
    private val dataSeeder: FirestoreDataSeeder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (BuildConfig.DEBUG) {
            lifecycleScope.launch {
                seedDataIfNeeded()
            }
        }

        setContent {
            LazyPizzaTheme {
                NavigationRoot(
                    navController = rememberNavController()
                )
            }
        }
    }

    private suspend fun seedDataIfNeeded() {
        val hasSeeded = appPreferences.hasSeededData()

        if (!hasSeeded) {
            Timber.d("🌱 Starting data seeding...")
            when (val result = dataSeeder.seedAll()) {
                is Result.Success -> {
                    Timber.d("✅ Data seeded successfully!")
                    appPreferences.setHasSeededData(true)
                }
                is Result.Error -> {
                    Timber.e("❌ Failed to seed data: ${result.error}")
                }
            }
        } else {
            Timber.d("✓ Data already seeded, skipping...")
        }
    }
}