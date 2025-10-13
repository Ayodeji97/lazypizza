package com.danzucker.lazypizza.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LazyPizzaPrimaryColor = Color(0xFFF36B50)
val LazyPizzaPrimaryColor8 = Color(0x14F36B50)
val LazyPizzaPrimaryColorAlpha8 = LazyPizzaPrimaryColor.copy(alpha = 0.08f)
val LazyPizzaTextPrimaryColor = Color(0xFF03131F)
val LazyPizzaTextOnPrimaryColor = Color(0xFFFFFFFF)
val LazyPizzaTextSecondaryColor = Color(0xFF627686)
val LazyPizzaTextSecondaryColor8 = Color(0x14627666)
val LazyPizzaTextSecondaryColorAlpha8 = LazyPizzaTextSecondaryColor.copy(alpha = 0.8f)
val LazyPizzaBackgroundColor = Color(0xFFFAFBFC)

val LazyPizzaSurfaceHigherColor = Color(0xFFFFFFFF)
val LazyPizzaSurfaceHighestColor = Color(0xFFF0F3F6)
val LazyPizzaOutlineColor50 = Color(0x80E6E7ED)
val LazyPizzaOutlineColorAlpha50 = LazyPizzaBackgroundColor.copy(alpha = 0.5f)
val LazyPizzaGradientSolidColor = Color(0xFFB23A24)
val LazyPizzaGradientLightColor = Color(0xFFF36B50)
val LazyPizzaShadowColor = Color(0x0F03131F)



val ColorScheme.LazyPizzaButtonGradient: Brush
    get() = Brush.linearGradient(
        colors = listOf(
            LazyPizzaGradientSolidColor,
            LazyPizzaGradientLightColor
        )
    )
