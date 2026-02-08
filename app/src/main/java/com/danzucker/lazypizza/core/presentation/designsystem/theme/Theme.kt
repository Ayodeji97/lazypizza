package com.danzucker.lazypizza.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


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