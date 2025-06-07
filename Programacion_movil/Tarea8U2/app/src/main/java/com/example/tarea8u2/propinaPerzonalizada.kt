package com.example.tarea8u2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tarea8u2.ui.theme.Tarea8U2Theme
import java.text.NumberFormat
// Clase principal que representa la actividad de la app
class propinaPerzonalizada : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Permite que la app se muestre a pantalla completa sin márgenes del sistema
        super.onCreate(savedInstanceState)

        // Establece la UI con Jetpack Compose
        setContent {
            Tarea8U2Theme {
                Surface( // Contenedor principal
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Tarea8U2Layout() // Llama al diseño principal de la app
                }
            }
        }
    }

    // Función que define el diseño de la pantalla
    @Composable
    fun Tarea8U2Layout() {
        var amountInput by remember { mutableStateOf("") } // Estado para el monto
        var tipInput by remember { mutableStateOf("") } // Estado para el porcentaje de propina
        var roundUp by remember { mutableStateOf(false) } // Estado del switch para redondear

        // Convierte el texto ingresado a Double o usa 0.0 si no es válido
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

        // Calcula la propina usando los valores actuales
        val tip = calculateTip(amount, tipPercent, roundUp)

        // Contenedor en forma de columna para los elementos
        Column(
            modifier = Modifier
                .statusBarsPadding() // Acomoda el diseño bajo la barra de estado
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()) // Permite scroll si el contenido es largo
                .safeDrawingPadding(), // Asegura padding seguro en pantallas recortadas
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la app
            Text(
                text = stringResource(R.string.calculate_tip),
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
                    .align(alignment = Alignment.Start)
            )

            // Campo de texto para el monto de la cuenta
            EditNumberField(
                label = R.string.bill_amount,
                leadingIcon = R.drawable.money, // Ícono de dinero
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, // Solo números
                    imeAction = ImeAction.Next // Avanzar al siguiente campo
                ),
                value = amountInput,
                onValueChanged = { amountInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            // Campo de texto para el porcentaje de propina
            EditNumberField(
                label = R.string.how_was_the_service,
                leadingIcon = R.drawable.percent, // Ícono de porcentaje
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done // Finaliza la entrada
                ),
                value = tipInput,
                onValueChanged = { tipInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            // Switch para activar o desactivar el redondeo de la propina
            RoundTheTipRow(
                roundUp = roundUp,
                onRoundUpChanged = { roundUp = it },
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Muestra el resultado de la propina formateado en moneda local
            Text(
                text = stringResource(R.string.tip_amount, tip),
                style = MaterialTheme.typography.displaySmall
            )

            // Espacio extra inferior
            Spacer(modifier = Modifier.height(150.dp))
        }
    }

    // Campo de texto reutilizable con ícono, etiqueta y opciones de teclado
    @Composable
    fun EditNumberField(
        @StringRes label: Int,
        @DrawableRes leadingIcon: Int,
        keyboardOptions: KeyboardOptions,
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        TextField(
            value = value,
            singleLine = true,
            leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
            modifier = modifier,
            onValueChange = onValueChanged,
            label = { Text(stringResource(label)) },
            keyboardOptions = keyboardOptions
        )
    }

    // Fila con un texto y un interruptor (Switch) para activar el redondeo
    @Composable
    fun RoundTheTipRow(
        roundUp: Boolean,
        onRoundUpChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.round_up_tip))
            Switch(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                checked = roundUp,
                onCheckedChange = onRoundUpChanged
            )
        }
    }

    // Función que calcula la propina y la devuelve en formato de moneda
    private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
        var tip = tipPercent / 100 * amount
        if (roundUp) {
            tip = kotlin.math.ceil(tip) // Redondea hacia arriba si se activó la opción
        }
        return NumberFormat.getCurrencyInstance().format(tip) // Devuelve el tip como "$X.XX"
    }
}

