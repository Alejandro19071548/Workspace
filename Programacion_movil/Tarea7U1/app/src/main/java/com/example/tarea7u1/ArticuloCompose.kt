package com.example.tarea7u1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea7u1.ui.theme.Tarea7U1Theme
class ArticuloCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tarea7U1Theme {
                // Contenedor base usando el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArticuloComposeApp()
                }
            }
        }
    }
}

// Función composable que representa la pantalla del artículo
@Composable
fun ArticuloComposeApp() {
    // Llama a la tarjeta del artículo con sus datos
    ArticleCard(
        title = stringResource(R.string.title_jetpack_compose_tutorial), // título desde strings.xml
        shortDescription = stringResource(R.string.compose_short_desc), // descripción corta
        longDescription = stringResource(R.string.compose_long_desc), // descripción larga
        imagePainter = painterResource(R.drawable.bg_compose_background) // imagen desde drawable
    )
}

// Función composable que muestra la tarjeta con imagen y texto
@Composable
private fun ArticleCard(
    title: String,
    shortDescription: String,
    longDescription: String,
    imagePainter: androidx.compose.ui.graphics.painter.Painter,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Image(painter = imagePainter, contentDescription = null)
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp
        )
        Text(
            text = shortDescription,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = longDescription,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Justify
        )
    }
}