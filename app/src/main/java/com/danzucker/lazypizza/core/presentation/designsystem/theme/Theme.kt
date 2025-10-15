package com.danzucker.lazypizza.core.presentation.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = LazyPizzaPrimaryColor,
    onPrimary = LazyPizzaTextOnPrimaryColor,
    secondary = LazyPizzaPrimaryColor8,
    background = LazyPizzaBackgroundColor,
    surface = LazyPizzaSurfaceHigherColor,
    onSurface = LazyPizzaTextPrimaryColor,
    surfaceVariant = LazyPizzaSurfaceHighestColor,
    surfaceTint = LazyPizzaTextSecondaryColor,
    outline = LazyPizzaBackgroundColor,
    outlineVariant = LazyPizzaOutlineColor50,
    inverseSurface = LazyPizzaGrayColor
)

@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}