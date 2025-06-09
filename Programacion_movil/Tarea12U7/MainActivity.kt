/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use este archivo excepto conforme al License.
 * Puedes obtener una copia del License en:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * A menos que la ley lo requiera o se acuerde por escrito, el software
 * distribuido bajo este License se distribuye en forma “AS IS”,
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO.
 * Para más detalles, consulta el License.
 */

package com.example.waterme

// Importa clases necesarias para Activity y Compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.waterme.ui.WaterMeApp
import com.example.waterme.ui.theme.WaterMeTheme

/**
 * MainActivity: punto de entrada de la aplicación WaterMe.
 * Hereda de ComponentActivity para usar Compose.
 *
 * Esta es la Activity principal que inicializa la interfaz de usuario de Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Habilita edge-to-edge para que la UI utilice todo el espacio de pantalla.
        // Esto permite que el contenido de la aplicación se dibuje detrás de las barras del sistema
        // (barra de estado y barra de navegación) para una experiencia más inmersiva.
        enableEdgeToEdge()
        // Llama al método onCreate de la superclase. Esto es crucial para el ciclo de vida de la Activity.
        super.onCreate(savedInstanceState)
        // setContent define el contenido Compose de esta Activity.
        // Todo lo que está dentro de este bloque es la jerarquía de UI de la aplicación.
        setContent {
            // Aplica el tema de la app (colores, tipografía, etc.) definido en `WaterMeTheme`.
            // Esto asegura una consistencia visual en toda la aplicación.
            WaterMeTheme {
                // Surface es un contenedor que ocupa todo el espacio disponible.
                // Se utiliza para definir un fondo y aplicar el color de fondo del tema.
                Surface(
                    modifier = Modifier.fillMaxSize(), // El modificador `fillMaxSize()` hace que la superficie ocupe todo el ancho y alto disponibles.
                    color = MaterialTheme.colorScheme.background // Se establece el color de fondo de la superficie al color de fondo definido en el tema de Material 3.
                ) {
                    // Llama al composable raíz de la app, `WaterMeApp`.
                    // Aquí es donde comienza la construcción de la interfaz de usuario de la aplicación.
                    WaterMeApp()
                }
            }
        }
    }
}