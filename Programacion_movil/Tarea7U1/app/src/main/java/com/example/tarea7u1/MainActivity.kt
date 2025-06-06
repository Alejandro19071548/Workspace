package com.example.tarea7u1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.tarea7u1.ui.theme.Tarea7U1Theme
// Clase principal de la aplicación, hereda de ComponentActivity
class MainActivity : ComponentActivity() {
    // Método que se ejecuta al iniciar la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa un modo de diseño sin bordes (útil para pantallas modernas)
        enableEdgeToEdge()

        // Establece el contenido de la interfaz usando Jetpack Compose
        setContent {
            // Aplica el tema personalizado de la app
            Tarea7U1Theme {
                // Scaffold permite una estructura básica de pantalla (barra, fondo, etc.)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llama al composable Greeting y le pasa un nombre
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Función composable que muestra un saludo en pantalla
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Box permite posicionar elementos en pantalla
    Box(
        modifier = Modifier
            .fillMaxSize() // Hace que el Box ocupe toda la pantalla
            .wrapContentSize(Alignment.Center) // Centra su contenido
    ) {
        // Muestra el texto "Hello Android!" con color azul
        Text(
            text = "Hello $name!",
            color = Color.Blue, // Cambia el color del texto
            modifier = modifier // Aplica el padding recibido
        )
    }
}

// Vista previa para mostrar el composable en el editor de Android Studio
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Tarea7U1Theme {
        Greeting("Android")
    }
}