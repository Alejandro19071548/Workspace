/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.affirmations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmations.data.Datasource
import com.example.affirmations.model.Affirmation
import com.example.affirmations.ui.theme.AffirmationsTheme
// Clase principal que hereda de ComponentActivity, la actividad base en Jetpack Compose
class MainActivity : ComponentActivity() {

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se aplica el tema personalizado de la app
            AffirmationsTheme {
                // Contenedor Surface que usa el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
                ) {
                    // Se llama a la función principal de la interfaz de usuario
                    AffirmationsApp()
                }
            }
        }
    }
}

// Función composable principal que inicia la lista de afirmaciones
@Composable
fun AffirmationsApp() {
    AffirmationList(
        affirmationList = Datasource().loadAffirmations(), // Carga la lista de afirmaciones desde el datasource
    )
}

// Función composable que muestra una lista desplazable de tarjetas de afirmaciones
@Composable
fun AffirmationList(affirmationList: List<Affirmation>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) { // Lista eficiente para scroll vertical
        items(affirmationList) { affirmation -> // Itera sobre cada afirmación
            AffirmationCard(
                affirmation = affirmation,
                modifier = Modifier.padding(8.dp) // Aplica un padding alrededor de cada tarjeta
            )
        }
    }
}

// Función composable que representa cada tarjeta de afirmación
@Composable
fun AffirmationCard(affirmation: Affirmation, modifier: Modifier = Modifier) {
    Card(modifier = modifier) { // Componente de tarjeta visual
        Column {
            Image(
                painter = painterResource(affirmation.imageResourceId), // Carga la imagen por ID
                contentDescription = stringResource(affirmation.stringResourceId), // Descripción accesible
                modifier = Modifier
                    .fillMaxWidth() // Ocupa el ancho completo
                    .height(194.dp), // Altura fija de la imagen
                contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
            )
            Text(
                text = LocalContext.current.getString(affirmation.stringResourceId), // Carga el texto por ID
                modifier = Modifier.padding(16.dp), // Padding interior del texto
                style = MaterialTheme.typography.headlineSmall // Estilo de texto definido en el tema
            )
        }
    }
}

// Función para previsualizar una tarjeta de afirmación en el editor de Android Studio
@Preview
@Composable
private fun AffirmationCardPreview() {
    AffirmationCard(Affirmation(R.string.affirmation1, R.drawable.image1)) // Muestra una afirmación de ejemplo
}
