package com.example.tarea7u1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea7u1.ui.theme.Tarea7U1Theme

// Define una actividad llamada ComposeQuadrant que extiende ComponentActivity
class ComposeQuadrant : ComponentActivity() {
    // Se ejecuta cuando la actividad es creada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent: aquí se define la interfaz de usuario usando Jetpack Compose
        setContent {
            // Aplica el tema personalizado de tu app (definido en Theme.Tarea7U1)
            Tarea7U1Theme  {

                // Surface: contenedor de fondo que aplica el color del tema
                Surface(
                    modifier = Modifier.fillMaxSize(), // ocupa toda la pantalla
                    color = MaterialTheme.colorScheme.background // usa el color de fondo del tema
                ) {
                    // Llama a una función @Composable que contiene el diseño principal
                    Tarea7U1Screen()
                }
            }
        }
    }
}
// Función @Composable que representa la pantalla principal
@Composable
fun Tarea7U1Screen() {
    // Crea una columna vertical que centra su contenido en ambas direcciones
    Column(
        modifier = Modifier
            .fillMaxWidth() // ocupa todo el ancho
            .fillMaxHeight(), // ocupa todo el alto
        verticalArrangement = Arrangement.Center, // centra el contenido verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // centra el contenido horizontalmente
    ) {
        // Carga una imagen desde los recursos drawable (ic_task_completed)
        val image = painterResource(R.drawable.ic_task_completed)

        // Muestra la imagen en la pantalla
        Image(painter = image, contentDescription = null)

        // Muestra un texto con estilo negrita y espacio de separación
        Text(
            text = stringResource(R.string.all_task_completed), // "All tasks completed"
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp), // espacio arriba y abajo
            fontWeight = FontWeight.Bold // negrita
        )

        // Muestra otro texto más pequeño
        Text(
            text = stringResource(R.string.nice_work), // "Nice work!"
            fontSize = 16.sp // tamaño de letra
        )
    }
}
