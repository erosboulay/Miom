package com.example.miom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


private val LightColorScheme = lightColorScheme(
    primary = BrandMedium,
)

@Composable
fun MiomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    CompositionLocalProvider(
        LocalRippleConfiguration provides null,
    ){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}