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

package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.woof.data.Dog
import com.example.woof.data.dogs
import com.example.woof.ui.theme.WoofTheme
// Clase principal de la aplicación, hereda de ComponentActivity (compatible con Jetpack Compose)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent establece el contenido de la UI usando Jetpack Compose
        setContent {
            WoofTheme { // Aplica el tema visual definido para la app
                // Surface es un contenedor que aplica el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize() // Ocupa todo el tamaño de la pantalla
                ) {
                    WoofApp() // Llama a la función que construye la interfaz principal
                }
            }
        }
    }
}

/**
 * Función composable que muestra una lista vertical de perros.
 */
@Composable
fun WoofApp() {
    LazyColumn { // Lista eficiente y desplazable de elementos
        items(dogs) { // Itera sobre la lista 'dogs'
            DogItem(dog = it) // Muestra cada perro usando DogItem
        }
    }
}

/**
 * Muestra un ítem individual de perro: ícono e información.
 *
 * @param dog contiene los datos del perro
 * @param modifier modificadores para personalizar la apariencia
 */
@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth() // Hace que la fila ocupe todo el ancho disponible
            .padding(dimensionResource(R.dimen.padding_small)) // Aplica padding
    ) {
        DogIcon(dog.imageResourceId) // Muestra la imagen del perro
        DogInformation(dog.name, dog.age) // Muestra el nombre y la edad del perro
    }
}

/**
 * Muestra la imagen del perro.
 *
 * @param dogIcon es el ID del recurso de imagen del perro
 * @param modifier modificadores para la imagen
 */
@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size)) // Tamaño fijo desde recursos
            .padding(dimensionResource(R.dimen.padding_small)), // Padding alrededor de la imagen
        painter = painterResource(dogIcon), // Carga la imagen desde recursos
        contentDescription = null // Al ser decorativa, se omite descripción accesible
    )
}

/**
 * Muestra el nombre y la edad del perro en texto.
 *
 * @param dogName ID del recurso de texto con el nombre
 * @param dogAge número entero con la edad del perro
 * @param modifier modificadores para el contenedor
 */
@Composable
fun DogInformation(
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) { // Organiza nombre y edad en una columna vertical
        Text(
            text = stringResource(dogName), // Obtiene el nombre desde recursos
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)) // Padding superior
        )
        Text(
            text = stringResource(R.string.years_old, dogAge), // Texto con la edad interpolada
        )
    }
}

/**
 * Vista previa del diseño de la app en modo claro.
 */
@Preview
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp() // Llama a la función principal para ver cómo se verá la app
    }
}
