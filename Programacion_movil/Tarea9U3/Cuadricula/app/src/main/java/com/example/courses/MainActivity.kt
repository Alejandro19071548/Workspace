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
package com.example.courses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courses.data.DataSource
import com.example.courses.model.Topic
import com.example.courses.ui.theme.CoursesTheme
// Clase principal que hereda de ComponentActivity (la base para actividades en Jetpack Compose)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Permite que el contenido se dibuje hasta los bordes de la pantalla
        super.onCreate(savedInstanceState)
        setContent {
            CoursesTheme { // Aplica el tema personalizado de la app
                // Contenedor Surface que usa el color de fondo del tema
                Surface(
                    modifier = Modifier
                        .fillMaxSize() // Ocupa todo el alto y ancho de la pantalla
                        .statusBarsPadding(), // Agrega padding para no superponerse con la barra de estado
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo definido en el tema
                ) {
                    // Llama a la función que muestra el grid de tarjetas de temas
                    TopicGrid(
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.padding_small),
                            top = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small),
                        )
                    )
                }
            }
        }
    }
}

// Función composable que construye una cuadrícula vertical (2 columnas) de tarjetas
@Composable
fun TopicGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Fija el número de columnas a 2
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)), // Espaciado vertical entre elementos
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)), // Espaciado horizontal entre columnas
        modifier = modifier
    ) {
        // Itera sobre la lista de temas y crea una tarjeta para cada uno
        items(DataSource.topics) { topic ->
            TopicCard(topic)
        }
    }
}

// Función composable que muestra una tarjeta con imagen, nombre del tema y número de cursos
@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Card { // Tarjeta visual que contiene el contenido del tema
        Row { // Organiza los elementos en una fila: imagen a la izquierda y texto a la derecha
            Box {
                Image(
                    painter = painterResource(id = topic.imageRes), // Carga la imagen del recurso
                    contentDescription = null, // No hay descripción accesible (se puede mejorar)
                    modifier = modifier
                        .size(width = 68.dp, height = 68.dp) // Tamaño fijo
                        .aspectRatio(1f), // Relación de aspecto 1:1 (cuadrado)
                    contentScale = ContentScale.Crop // Recorta la imagen para llenar el espacio
                )
            }

            Column { // Contiene el nombre del tema y el número de cursos disponibles
                Text(
                    text = stringResource(id = topic.name), // Muestra el nombre del tema
                    style = MaterialTheme.typography.bodyMedium, // Estilo de texto medio
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_small)
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) { // Ícono y número de cursos
                    Icon(
                        painter = painterResource(R.drawable.ic_grain), // Ícono decorativo
                        contentDescription = null, // Sin descripción accesible
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.padding_medium)) // Padding a la izquierda
                    )
                    Text(
                        text = topic.availableCourses.toString(), // Muestra la cantidad de cursos
                        style = MaterialTheme.typography.labelMedium, // Estilo para etiquetas
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small)) // Separación entre ícono y texto
                    )
                }
            }
        }
    }
}

// Función de previsualización para ver una tarjeta de tema en el editor de Android Studio
@Preview(showBackground = true)
@Composable
fun TopicPreview() {
    CoursesTheme { // Aplica el tema visual
        val topic = Topic(R.string.photography, 321, R.drawable.photography) // Crea un tema de ejemplo
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Centra verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
        ) {
            TopicCard(topic = topic) // Muestra la tarjeta de ejemplo
        }
    }
}
