package com.example.miom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GreyLightest,
    secondary = GreyLightest,
    tertiary = GreyLightest,
    surface = GreyLightest,

    )

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = GreyLightest,
    tertiary = GreyLightest,

    surface = GreyLightest,

)

@Composable
fun MiomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}