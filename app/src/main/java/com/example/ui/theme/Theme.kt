package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NoraPrimaryPurple,
    onPrimary = NoraWhite,
    primaryContainer = NoraElectricViolet,
    onPrimaryContainer = NoraWhite,
    secondary = NoraSkyCyan,
    onSecondary = NoraDarkNavy,
    secondaryContainer = NoraDarkSurfaceVariant,
    onSecondaryContainer = NoraSkyCyan,
    tertiary = NoraCoralPink,
    onTertiary = NoraWhite,
    background = NoraDeepBackground,
    onBackground = NoraTextPrimaryDark,
    surface = NoraDarkSurface,
    onSurface = NoraTextPrimaryDark,
    surfaceVariant = NoraDarkSurfaceVariant,
    onSurfaceVariant = NoraTextSecondaryDark,
    error = NoraError,
    onError = NoraWhite
)

private val LightColorScheme = lightColorScheme(
    primary = NoraPrimaryPurple,
    onPrimary = NoraWhite,
    primaryContainer = NoraSoftLavender,
    onPrimaryContainer = NoraPrimaryPurple,
    secondary = NoraSkyCyan,
    onSecondary = NoraDarkNavy,
    secondaryContainer = NoraSoftLavender,
    onSecondaryContainer = NoraDarkNavy,
    tertiary = NoraCoralPink,
    onTertiary = NoraWhite,
    background = NoraBgLight,
    onBackground = NoraTextPrimaryLight,
    surface = NoraCardBgLight,
    onSurface = NoraTextPrimaryLight,
    surfaceVariant = NoraSoftLavender,
    onSurfaceVariant = NoraTextSecondaryLight,
    error = NoraError,
    onError = NoraWhite
)

@Composable
fun NoraAiTheme(
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
