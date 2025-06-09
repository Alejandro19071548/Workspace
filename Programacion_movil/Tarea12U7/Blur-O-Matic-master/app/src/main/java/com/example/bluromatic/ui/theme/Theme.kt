/*
 * Archivo con licencia Apache 2.0
 * Este archivo define el tema de la app usando Material 3 (Jetpack Compose).
 */

package com.example.bluromatic.ui.theme

// Importaciones necesarias para la definición del tema
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores para tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    background = md_theme_dark_background,
)

// Paleta de colores para tema claro
private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    secondaryContainer = md_theme_light_secondaryContainer,
    background = md_theme_light_background,
)

/**
 * Función que aplica el tema Bluromatic a la interfaz.
 * - Usa tema claro u oscuro según el sistema.
 * - *Opcionalmente* usa colores dinámicos en Android 12+ (desactivado aquí).
 */
@Composable
fun BluromaticTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta automáticamente si el sistema está en modo oscuro
    dynamicColor: Boolean = false, // Variable para permitir colores dinámicos (desactivado por defecto)
    content: @Composable () -> Unit // Contenido Composable sobre el cual aplicar el tema
) {
    // Determina la paleta de colores a usar según las condiciones
    val colorScheme = when {
        // Si está habilitado el color dinámico y el dispositivo lo soporta (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Si no, usar tema claro u oscuro fijo
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Cambia el color de la barra de estado para que combine con el fondo
    val view = LocalView.current
    if (!view.isInEditMode) { // Evita que se aplique en el editor de vistas
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Aplica color de fondo al status bar
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Aplica el tema a todo el contenido proporcionado
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
