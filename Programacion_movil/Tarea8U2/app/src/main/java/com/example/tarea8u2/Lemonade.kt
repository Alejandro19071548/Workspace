package com.example.tarea8u2


// Importaciones necesarias para el funcionamiento de la app con Jetpack Compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.tarea8u2.ui.theme.Tarea8U2Theme
// Clase principal de la app, hereda de ComponentActivity
class Lemonade : ComponentActivity() {

    // Método que se ejecuta al iniciar la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Permite que la app use toda la pantalla, detrás de las barras del sistema
        super.onCreate(savedInstanceState)

        // Define la UI con Jetpack Compose
        setContent {
            Tarea8U2Theme { // Aplica el tema personalizado de la app
                Tarea8U2App() // Llama a la función que define la interfaz principal
            }
        }
    }

    // Marca que se usa una API experimental de Material 3
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Tarea8U2App() {
        // Estado para controlar el paso actual del proceso de hacer limonada
        var currentStep by remember { mutableStateOf(1) }

        // Estado que guarda cuántas veces hay que exprimir el limón (entre 2 y 4)
        var squeezeCount by remember { mutableStateOf(0) }

        // Scaffold proporciona una estructura base con barra superior
        Scaffold(
            topBar = {
                // Barra superior centrada con título
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Lemonade", // Título de la app
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { innerPadding ->

            // Contenedor principal de la pantalla
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Evita que el contenido choque con la barra superior
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                color = MaterialTheme.colorScheme.background
            ) {

                // Controla qué pantalla mostrar según el paso actual
                when (currentStep) {

                    1 -> {
                        // Paso 1: seleccionar un limón
                        LemonTextAndImage(
                            textLabelResourceId = R.string.lemon_select, // Texto instructivo
                            drawableResourceId = R.drawable.lemon_tree, // Imagen del árbol de limones
                            contentDescriptionResourceId = R.string.lemon_tree_content_description, // Descripción para accesibilidad
                            onImageClick = {
                                currentStep = 2 // Avanza al siguiente paso
                                squeezeCount = (2..4).random() // Decide cuántas veces exprimir
                            }
                        )
                    }

                    2 -> {
                        // Paso 2: exprimir el limón
                        LemonTextAndImage(
                            textLabelResourceId = R.string.lemon_squeeze,
                            drawableResourceId = R.drawable.lemon_squeeze,
                            contentDescriptionResourceId = R.string.lemon_content_description,
                            onImageClick = {
                                squeezeCount-- // Resta una exprimida
                                if (squeezeCount == 0) {
                                    currentStep = 3 // Si ya exprimió lo suficiente, pasa al siguiente paso
                                }
                            }
                        )
                    }

                    3 -> {
                        // Paso 3: beber la limonada
                        LemonTextAndImage(
                            textLabelResourceId = R.string.lemon_drink,
                            drawableResourceId = R.drawable.lemon_drink,
                            contentDescriptionResourceId = R.string.lemonade_content_description,
                            onImageClick = {
                                currentStep = 4 // Paso final
                            }
                        )
                    }

                    4 -> {
                        // Paso 4: reiniciar el proceso
                        LemonTextAndImage(
                            textLabelResourceId = R.string.lemon_empty_glass,
                            drawableResourceId = R.drawable.lemon_restart,
                            contentDescriptionResourceId = R.string.empty_glass_content_description,
                            onImageClick = {
                                currentStep = 1 // Vuelve al inicio
                            }
                        )
                    }
                }
            }
        }
    }

    // Función composable reutilizable que muestra imagen + texto
    @Composable
    fun LemonTextAndImage(
        textLabelResourceId: Int, // ID del texto a mostrar
        drawableResourceId: Int, // ID de la imagen
        contentDescriptionResourceId: Int, // ID de descripción de imagen para accesibilidad
        onImageClick: () -> Unit, // Acción al hacer clic
        modifier: Modifier = Modifier // Modificador opcional
    ) {
        Box(modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // Botón con la imagen dentro
                Button(
                    onClick = onImageClick, // Ejecuta la acción correspondiente al paso
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    // Imagen que representa el paso actual
                    Image(
                        painter = painterResource(drawableResourceId),
                        contentDescription = stringResource(contentDescriptionResourceId),
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.button_image_width))
                            .height(dimensionResource(R.dimen.button_image_height))
                            .padding(dimensionResource(R.dimen.button_interior_padding))
                    )
                }

                // Espacio entre imagen y texto
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_vertical)))

                // Texto debajo de la imagen
                Text(
                    text = stringResource(textLabelResourceId),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
