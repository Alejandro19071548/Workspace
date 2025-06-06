package com.example.tarea7u1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea7u1.ui.theme.Tarea7U1Theme
// Clase principal que extiende de ComponentActivity para crear una actividad de Jetpack Compose
class TarjetaPresentacion : ComponentActivity() {
    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el contenido de la actividad con Compose
        setContent {
            // Aplica el tema personalizado Tarea7U1Theme
            Tarea7U1Theme {
                // Crea un contenedor Surface que ocupa toda la pantalla con el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama a la función composable principal de la tarjeta de presentación
                    BusinessCardApp()
                }
            }
        }
    }
}

// Función composable que representa la tarjeta de presentación completa
@Composable
fun BusinessCardApp() {
    // Crea una columna que organiza los elementos de arriba hacia abajo
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el alto y ancho disponible
            .padding(16.dp), // Aplica un relleno de 16dp
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
        verticalArrangement = Arrangement.Center // Centra los elementos verticalmente
    ) {
        // Parte superior: imagen del logo
        Image(
            painter = painterResource(R.drawable.android_logo), // Recurso de imagen desde drawable
            contentDescription = null, // Descripción omitida por ser decorativa
            modifier = Modifier.size(100.dp) // Tamaño de la imagen
        )
        // Nombre de la persona
        Text(
            text = "Erika Del Angel", // Texto del nombre
            fontSize = 24.sp, // Tamaño de fuente
            color = Color(0xFF3ddc84) // Color verde personalizado
        )
        // Cargo o profesión
        Text(
            text = "Desarrolladora Android", // Texto del cargo
            fontSize = 18.sp, // Tamaño de fuente
            color = Color.DarkGray // Color gris oscuro
        )

        // Espacio entre la parte superior y la inferior
        Spacer(modifier = Modifier.height(40.dp))

        // Parte inferior: información de contacto
        ContactInfo(icon = Icons.Default.Phone, contactText = "833 463 1632") // Teléfono
        ContactInfo(icon = Icons.Default.Email, contactText = "erika@email.com") // Correo
    }
}

// Función composable que representa una fila con un ícono y texto para información de contacto
@Composable
fun ContactInfo(icon: androidx.compose.ui.graphics.vector.ImageVector, contactText: String) {
    // Fila para alinear el ícono y el texto horizontalmente
    Row(
        verticalAlignment = Alignment.CenterVertically, // Centra verticalmente el contenido
        modifier = Modifier.padding(vertical = 8.dp) // Aplica espacio arriba y abajo
    ) {
        // Ícono del tipo de contacto (teléfono o correo)
        Icon(
            imageVector = icon, // Ícono recibido como parámetro
            contentDescription = null, // Descripción omitida por ser decorativa
            tint = Color(0xFF3ddc84), // Color verde personalizado
            modifier = Modifier.padding(end = 8.dp) // Espacio a la derecha del ícono
        )
        // Texto del contacto (teléfono o correo)
        Text(text = contactText)
    }
}
