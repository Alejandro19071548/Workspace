package com.example.tarea7u1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea7u1.ui.theme.Tarea7U1Theme

// Clase principal de la actividad de cumpleaños
class happybirthday : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplica el tema de la app
            Tarea7U1Theme {
                // Contenedor de fondo que ocupa toda la pantalla y usa el color del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background // <- aquí deberías usar MaterialTheme.colorScheme.background
                ) {
                    // Llama al composable GreetingText con los mensajes
                    GreetingText(
                        message = "Happy Birthday Sam!",
                        from = "From Emma",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

// Composable que muestra los textos de felicitación
@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center, // Alinea verticalmente al centro
        modifier = modifier // Aplica los modificadores que se pasen por parámetro
    ) {
        Text(
            text = message, // Texto del mensaje principal
            fontSize = 100.sp, // Tamaño grande para el texto
            lineHeight = 116.sp, // Altura de línea
            textAlign = TextAlign.Center // Texto centrado horizontalmente
        )
        Text(
            text = from, // Texto del remitente
            fontSize = 36.sp, // Tamaño más pequeño
            modifier = Modifier
                .padding(16.dp) // Margen alrededor del texto
                .align(alignment = Alignment.End) // Alinea el texto al final (derecha)
        )
    }
}

// Función de vista previa para mostrar el diseño sin ejecutar la app
@Preview(showBackground = true)
@Composable
fun BirthdayCardPreview() {
    Tarea7U1Theme {
        GreetingText(message = "Happy Birthday Sam!", from = "From Emma")
    }
}