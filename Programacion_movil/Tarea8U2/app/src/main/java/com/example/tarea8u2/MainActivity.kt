package com.example.tarea8u2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tarea8u2.ui.theme.Tarea8U2Theme
// Actividad principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el contenido de la interfaz de usuario usando Jetpack Compose
        setContent {
            // Aplica el tema personalizado de la aplicación
            Tarea8U2Theme {
                // Surface actúa como contenedor principal con fondo temático
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
                ) {
                    // Llama al componente principal de la app (composable)
                    Tarea8U2App()
                }
            }
        }
    }
}

// Anotación que permite previsualizar la interfaz en Android Studio
@Preview
@Composable
fun Tarea8U2App() {
    // Llama al componente que contiene la lógica del dado y el botón
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible
            .wrapContentSize(Alignment.Center) // Centra el contenido en la pantalla
    )
}

// Función composable que representa el dado y el botón para lanzarlo
@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    // Variable de estado que guarda el valor actual del dado (inicialmente 1)
    var result by remember { mutableStateOf(1) }

    // Según el valor del dado, asigna la imagen correspondiente
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6 // Por defecto, imagen del 6
    }

    // Organiza los elementos en columna y los centra horizontalmente
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Muestra la imagen del dado correspondiente al valor actual
        Image(
            painter = painterResource(imageResource), // Carga la imagen desde los recursos
            contentDescription = result.toString() // Descripción para accesibilidad
        )

        // Botón que al hacer clic lanza el dado (genera número aleatorio)
        Button(
            onClick = { result = (1..6).random() } // Cambia el valor del dado al azar
        ) {
            // Texto del botón (como "Lanzar" o "Roll")
            Text(
                text = stringResource(R.string.roll), // Toma el texto desde los recursos de strings
                fontSize = 24.sp // Establece el tamaño del texto
            )
        }
    }
}
