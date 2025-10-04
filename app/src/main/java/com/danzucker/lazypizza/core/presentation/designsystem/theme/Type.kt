package com.danzucker.lazypizza.core.presentation.designsystem.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.danzucker.lazypizza.R


val InstrumentSans = FontFamily(
    Font(
        resId = R.font.instrument_sans_regular,
        style = FontStyle.Normal,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.instrument_sans_medium,
        style = FontStyle.Normal,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.instrument_sans_semi_bold,
        style = FontStyle.Normal,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.instrument_sans_bold,
        style = FontStyle.Normal,
        weight = FontWeight.Bold
    )
)

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
)