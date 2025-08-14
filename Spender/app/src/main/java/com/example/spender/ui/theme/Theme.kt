package com.example.spender.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkModePointColor,
    onPrimary = WhiteColor,
    primaryContainer = DarkModeSelectedColor,
    secondary = DarkModePointColor,
    onSecondary = WhiteColor,
    tertiary= DarkModeTabColor,
    onTertiary= DarkModeLightFontColor,
    background = DarkModeBackground,
    onBackground = DarkModeDefaultFontColor,
    surface = DarkModeLightSurface,
    onSurface = DarkModeDefaultFontColor,
    outline = DarkModeTabColor,
    error = PointRedColor
)

private val LightColorScheme = lightColorScheme(
    primary = PointColor,
    onPrimary = WhiteColor,
    primaryContainer = SelectedColor,
    secondary = LightGray,
    onSecondary = WhiteColor,
    tertiary= TabColor,
    onTertiary= LightFontColor,
    background = WhiteColor,
    onBackground = DefaultFontColor,
    surface = LightSurface,
    onSurface = DefaultFontColor,
    outline = TabColor,
    error = PointRedColor
)

@Composable
fun SpenderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}