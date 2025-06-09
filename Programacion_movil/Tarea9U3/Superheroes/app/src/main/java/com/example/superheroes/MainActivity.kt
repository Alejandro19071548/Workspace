/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.superheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.superheroes.model.HeroesRepository
import com.example.superheroes.ui.theme.SuperheroesTheme
// Clase principal de la app, hereda de ComponentActivity para usar Jetpack Compose
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita que el contenido llegue hasta los bordes de la pantalla
        setContent {
            SuperheroesTheme { // Aplica el tema definido para la app
                // Contenedor de superficie con color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa todo el tamaño disponible
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
                ) {
                    SuperheroesApp() // Llama al composable principal de la app
                }
            }
        }
    }

    /**
     * Función composable principal que muestra la barra superior y la lista de héroes.
     */
    @Composable
    fun SuperheroesApp() {
        Scaffold( // Componente que proporciona estructura visual: barra superior, contenido, etc.
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar() // Barra superior de la app
            }
        ) {
            /* Nota: No es buena práctica acceder directamente a datos en la UI.
               Más adelante se enseña cómo usar ViewModel para separar lógica de datos.
             */
            val heroes = HeroesRepository.heroes // Obtiene lista de héroes desde el repositorio
            HeroesList(heroes = heroes, contentPadding = it) // Muestra la lista, con padding del Scaffold
        }
    }

    /**
     * Función composable que muestra una barra superior centrada con texto (nombre de la app).
     *
     * @param modifier permite modificar la apariencia del componente
     */
    @OptIn(ExperimentalMaterial3Api::class) // Usa una API experimental de Material3
    @Composable
    fun TopAppBar(modifier: Modifier = Modifier) {
        CenterAlignedTopAppBar( // Barra superior con título centrado
            title = {
                Text(
                    text = stringResource(R.string.app_name), // Toma el nombre desde recursos
                    style = MaterialTheme.typography.displayLarge, // Usa estilo tipográfico del tema
                )
            },
            modifier = modifier
        )
    }

    /**
     * Vista previa del composable SuperheroesApp con el tema aplicado.
     */
    @Preview(showBackground = true)
    @Composable
    fun SuperHeroesPreview() {
        SuperheroesTheme {
            SuperheroesApp() // Renderiza la app para previsualización en Android Studio
        }
    }
}
