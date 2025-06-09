/*
 * Definición del tema visual de la aplicación usando Material Design 3 (Material You).
 * Se establecen esquemas de color para modo claro y oscuro, con soporte para colores dinámicos
 * en Android 12+ (aunque está desactivado en este ejemplo para fines didácticos).
 */

package com.example.waterme.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Esquema de color para el modo oscuro, utilizando colores definidos en el archivo de colores
private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    surfaceVariant = md_theme_dark_surfaceVariant,
)

// Esquema de color para el modo claro, utilizando colores definidos en el archivo de colores
private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    surfaceVariant = md_theme_light_surfaceVariant,
)

@Composable
fun WaterMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // La opción para activar colores dinámicos de Android 12+ está desactivada para aprendizaje
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Selección del esquema de color según el modo y si se usan colores dinámicos
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Aplicación del esquema de color con MaterialTheme para el contenido UI
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
