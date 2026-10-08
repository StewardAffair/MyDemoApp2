package com.roman.mydemoapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val defaultTypography = Typography()

val AppTypography = defaultTypography.copy(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = defaultTypography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontSize = 15.sp,
        lineHeight = 21.sp
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    )
)
