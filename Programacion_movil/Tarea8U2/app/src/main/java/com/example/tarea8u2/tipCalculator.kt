package com.example.tarea8u2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tarea8u2.ui.theme.Tarea8U2Theme
import java.text.NumberFormat
// Clase principal que representa una actividad de la app
class tipCalculator : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Habilita el diseño de borde a borde (pantalla completa sin márgenes del sistema)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Define el contenido de la interfaz con Jetpack Compose
        setContent {
            // Aplica el tema visual definido en la app
            Tarea8U2Theme  {
                // Crea una superficie que ocupa toda la pantalla
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Llama a la función que muestra el diseño principal
                    Tarea8U2Layout()
                }
            }
        }
    }

    // Composable que define el diseño principal de la app
    @Composable
    fun Tarea8U2Layout() {
        // Variable para almacenar el texto ingresado por el usuario
        var amountInput by remember { mutableStateOf("") }

        // Convierte el texto a número. Si falla, se usa 0.0
        val amount = amountInput.toDoubleOrNull() ?: 0.0

        // Calcula la propina a partir del monto ingresado
        val tip = calculateTip(amount)

        // Organiza los elementos verticalmente con padding y scroll
        Column(
            modifier = Modifier
                .statusBarsPadding() // Evita que el contenido quede detrás de la barra de estado
                .padding(horizontal = 40.dp) // Margen a los lados
                .verticalScroll(rememberScrollState()) // Permite desplazamiento si hay mucho contenido
                .safeDrawingPadding(), // Asegura que los elementos no queden bajo gestos del sistema
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la pantalla
            Text(
                text = stringResource(R.string.calculate_tip),
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
                    .align(alignment = Alignment.Start)
            )

            // Campo de texto para escribir el monto de la cuenta
            EditNumberField(
                value = amountInput,
                onValueChange = { amountInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            // Muestra la propina calculada
            Text(
                text = stringResource(R.string.tip_amount, tip),
                style = MaterialTheme.typography.displaySmall
            )

            // Espaciador para dar margen inferior
            Spacer(modifier = Modifier.height(150.dp))
        }
    }

    // Composable que representa un campo de texto para ingresar números
    @Composable
    fun EditNumberField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            label = { Text(stringResource(R.string.bill_amount)) }, // Etiqueta: "Bill Amount"
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Teclado numérico
            modifier = modifier
        )
    }

    // Función para calcular la propina basada en el monto ingresado
    private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
        val tip = tipPercent / 100 * amount
        return NumberFormat.getCurrencyInstance().format(tip) // Retorna propina formateada ($)
    }
}

