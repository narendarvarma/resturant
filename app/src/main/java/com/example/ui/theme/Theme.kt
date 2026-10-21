package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AbhilashPrimary,
    onPrimary = Color.White,
    primaryContainer = AbhilashSurfaceVariant,
    onPrimaryContainer = AbhilashPrimaryDark,
    secondary = AbhilashSecondary,
    onSecondary = Color.White,
    tertiary = AbhilashGreen,
    onTertiary = Color.White,
    background = AbhilashBackground,
    onBackground = AbhilashCharcoal,
    surface = AbhilashSurface,
    onSurface = AbhilashCharcoal,
    surfaceVariant = AbhilashSurfaceVariant,
    onSurfaceVariant = AbhilashCharcoal,
    outline = AbhilashBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = AbhilashPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF431407),
    onPrimaryContainer = Color(0xFFFFEDD5),
    secondary = AbhilashSecondary,
    onSecondary = Color.White,
    tertiary = AbhilashGreen,
    onTertiary = Color.White,
    background = AbhilashDarkBackground,
    onBackground = AbhilashDarkText,
    surface = AbhilashDarkSurface,
    onSurface = AbhilashDarkText,
    surfaceVariant = Color(0xFF3F3F46),
    onSurfaceVariant = Color(0xFFE4E4E7),
    outline = Color(0xFF52525B)
)

@Composable
fun AbhilashRestaurantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AbhilashRestaurantTheme(darkTheme = darkTheme, content = content)
}
