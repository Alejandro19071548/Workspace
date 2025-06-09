/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * no puedes usar este archivo excepto conforme al License.
 * Puedes obtener una copia del License en:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * A menos que la ley lo requiera o se acuerde por escrito, el software
 * distribuido bajo este License se distribuye en forma “AS IS”,
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO.
 * Para más detalles, consulta el License.
 */

package com.example.waterme.ui

// Import de Compose para UI, ViewModel y utilidades
import androidx.compose.foundation.clickable // Modificador para hacer un elemento clickeable.
import androidx.compose.foundation.layout.Arrangement // Para organizar elementos hijos en un diseño.
import androidx.compose.foundation.layout.Column // Composable para organizar elementos verticalmente.
import androidx.compose.foundation.layout.PaddingValues // Define el padding en todos los lados.
import androidx.compose.foundation.layout.WindowInsets // Representa los insets de la ventana (e.g., barras de sistema).
import androidx.compose.foundation.layout.asPaddingValues // Convierte WindowInsets en PaddingValues.
import androidx.compose.foundation.layout.calculateEndPadding // Calcula el padding final según la dirección del layout.
import androidx.compose.foundation.layout.calculateStartPadding // Calcula el padding inicial según la dirección del layout.
import androidx.compose.foundation.layout.fillMaxSize // Modificador para ocupar el tamaño máximo disponible.
import androidx.compose.foundation.layout.fillMaxWidth // Modificador para ocupar el ancho máximo disponible.
import androidx.compose.foundation.layout.padding // Modificador para aplicar padding.
import androidx.compose.foundation.layout.safeDrawing // Insets para el área segura de dibujo.
import androidx.compose.foundation.layout.statusBarsPadding // Modificador para aplicar padding según la altura de la barra de estado.
import androidx.compose.foundation.lazy.LazyColumn // Un Composable eficiente para mostrar listas de elementos.
import androidx.compose.foundation.lazy.items // Extensión para LazyColumn para iterar sobre una lista de elementos.
import androidx.compose.material3.AlertDialog // Composable para mostrar un diálogo de alerta.
import androidx.compose.material3.Card // Composable para mostrar contenido en una tarjeta elevable.
import androidx.compose.material3.MaterialTheme.typography // Acceso a la tipografía del tema Material Design 3.
import androidx.compose.runtime.Composable // Anotación para indicar que una función es un Composable.
import androidx.compose.runtime.getValue // Delegate para observar cambios en el estado.
import androidx.compose.runtime.mutableStateOf // Función para crear un estado mutable.
import androidx.compose.runtime.saveable.rememberSaveable // Función para recordar el estado incluso después de cambios de configuración.
import androidx.compose.runtime.setValue // Delegate para actualizar el estado.
import com.example.waterme.data.Reminder // Data class que representa un recordatorio.
import com.example.waterme.model.Plant // Data class que representa una planta.
import androidx.compose.ui.Modifier // Interfaz para modificar la apariencia o comportamiento de los Composable.
import androidx.compose.material3.Surface // Un contenedor básico de Material Design.
import androidx.compose.material3.Text // Composable para mostrar texto.
import androidx.compose.ui.platform.LocalLayoutDirection // Composable para obtener la dirección del layout (LTR/RTL).
import androidx.compose.ui.res.dimensionResource // Función para cargar una dimensión desde los recursos.
import androidx.compose.ui.res.stringResource // Función para cargar un string desde los recursos.
import androidx.compose.ui.text.style.TextAlign // Para alinear el texto.
import androidx.compose.ui.unit.dp // Unidades de densidad de píxeles para Compose.
import androidx.lifecycle.viewmodel.compose.viewModel // Función de extensión para obtener un ViewModel en Compose.
import com.example.waterme.ui.theme.WaterMeTheme // El tema de la aplicación.
import com.example.waterme.FIVE_SECONDS // Constante para la duración de 5 segundos.
import com.example.waterme.ONE_DAY // Constante para la duración de 1 día.
import com.example.waterme.R // Clase generada automáticamente para acceder a los recursos.
import com.example.waterme.SEVEN_DAYS // Constante para la duración de 7 días.
import com.example.waterme.THIRTY_DAYS // Constante para la duración de 30 días.
import com.example.waterme.data.DataSource // Objeto que proporciona datos de plantas.
import androidx.compose.ui.tooling.preview.Preview // Anotación para crear vistas previas de Composable en Android Studio.
import java.util.concurrent.TimeUnit // Clase para representar unidades de tiempo.

/**
 * WaterMeApp: Composable raíz de la aplicación.
 * Obtiene el ViewModel y muestra la lista de plantas.
 *
 * Este es el punto de entrada principal de la interfaz de usuario de Compose.
 */
@Composable
fun WaterMeApp(waterViewModel: WaterViewModel = viewModel(factory = WaterViewModel.Factory)) {
    // Obtiene la dirección de layout (LTR/RTL) para cálculo de padding seguro.
    // Esto es importante para el soporte de idiomas de derecha a izquierda.
    val layoutDirection = LocalLayoutDirection.current
    // Aplica el tema de la app, definiendo los colores, tipografía, etc.
    WaterMeTheme {
        // Surface que aplica padding para barras de estado y áreas seguras.
        // `statusBarsPadding()` añade padding para evitar que el contenido se superponga con la barra de estado.
        // Los cálculos de `safeDrawing.asPaddingValues()` y `calculateStartPadding`/`calculateEndPadding`
        // aseguran que el contenido no se dibuje debajo de las barras del sistema o los recortes de pantalla.
        Surface(
            modifier = Modifier
                .fillMaxSize() // La superficie ocupa todo el espacio disponible.
                .statusBarsPadding()
                .padding(
                    start = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateEndPadding(layoutDirection)
                )
        ) {
            // Llama a PlantListContent, pasando las plantas del ViewModel y la función para programar recordatorios.
            // La lista de plantas se observa desde el `waterViewModel`, y `onScheduleReminder`
            // es una lambda que delega la acción al ViewModel.
            PlantListContent(
                plants = waterViewModel.plants, // La lista de plantas obtenida del ViewModel.
                onScheduleReminder = { waterViewModel.scheduleReminder(it) } // La función para programar un recordatorio.
            )
        }
    }
}

/**
 * PlantListContent: Composable que muestra una lista de cartas de plantas.
 *
 * Mantiene el estado de la planta seleccionada y si el diálogo de recordatorio debe mostrarse.
 *
 * @param plants Lista de Plant que se mostrará.
 * @param onScheduleReminder Lambda llamada cuando el usuario elige programar un recordatorio.
 * @param modifier Modificador Compose opcional.
 */
@Composable
fun PlantListContent(
    plants: List<Plant>,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para la planta seleccionada (por defecto la primera).
    // `rememberSaveable` asegura que este estado se preserve a través de cambios de configuración
    // como la rotación de la pantalla.
    var selectedPlant by rememberSaveable { mutableStateOf(plants[0]) }
    // Estado para mostrar/ocultar el diálogo de recordatorio.
    var showReminderDialog by rememberSaveable { mutableStateOf(false) }

    // LazyColumn para mostrar elementos de forma perezosa (scroll).
    // Es eficiente para listas largas ya que solo compone y recicla los elementos visibles.
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_medium)), // Padding alrededor del contenido de la lista.
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)), // Espacio entre los elementos verticalmente.
        modifier = modifier
    ) {
        // `items` itera sobre cada planta en la lista y compone un `PlantListItem` para cada una.
        items(items = plants) { plant ->
            PlantListItem(
                plant = plant,
                onItemSelect = { chosen ->
                    // Cuando se selecciona una planta, actualiza `selectedPlant` y marca para mostrar el diálogo.
                    selectedPlant = chosen
                    showReminderDialog = true
                },
                modifier = Modifier.fillMaxWidth() // Cada elemento ocupa el ancho completo.
            )
        }
    }

    // Muestra el diálogo de recordatorio si `showReminderDialog` es verdadero.
    // El diálogo se compone condicionalmente.
    if (showReminderDialog) {
        ReminderDialogContent(
            onDialogDismiss = { showReminderDialog = false }, // Función para cerrar el diálogo.
            plantName = stringResource(selectedPlant.name), // Nombre de la planta seleccionada.
            onScheduleReminder = onScheduleReminder // Función para programar el recordatorio.
        )
    }
}

/**
 * PlantListItem: Composable que muestra la tarjeta de una planta.
 *
 * @param plant         Plant que se mostrará.
 * @param onItemSelect  Lambda que se invoca cuando se hace clic en la tarjeta.
 * @param modifier      Modificador Compose opcional.
 */
@Composable
fun PlantListItem(plant: Plant, onItemSelect: (Plant) -> Unit, modifier: Modifier = Modifier) {
    // Card para mostrar la información de la planta, proporcionando una apariencia elevable y sombreada.
    Card(
        modifier = modifier
            .clickable { onItemSelect(plant) } // El modificador `clickable` hace que la tarjeta sea interactiva y llama a `onItemSelect` al hacer clic.
    ) {
        // Column para apilar textos verticalmente dentro de la tarjeta.
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium)) // Padding interno dentro de la tarjeta.
                .fillMaxWidth(), // La columna ocupa el ancho completo de la tarjeta.
            verticalArrangement = Arrangement.spacedBy(4.dp) // Separación vertical de 4.dp entre los elementos de texto.
        ) {
            // Nombre de la planta, centrado y en estilo headline.
            Text(
                text = stringResource(plant.name),
                modifier = Modifier.fillMaxWidth(),
                style = typography.headlineSmall, // Aplica un estilo de texto predefinido del tema.
                textAlign = TextAlign.Center // Centra el texto horizontalmente.
            )
            // Tipo de planta con estilo de título medio.
            Text(text = stringResource(plant.type), style = typography.titleMedium)
            // Descripción de la planta con estilo de título medio.
            Text(text = stringResource(plant.description), style = typography.titleMedium)
            // Frecuencia de riego: "Water X" (e.g., "Water every 3 days").
            Text(
                text = "${stringResource(R.string.water)} ${stringResource(plant.schedule)}",
                style = typography.titleMedium
            )
        }
    }
}

/**
 * ReminderDialogContent: Composable que muestra un diálogo para elegir intervalo de recordatorio.
 *
 * @param onDialogDismiss    Lambda que se invoca para cerrar el diálogo.
 * @param plantName          Nombre de la planta seleccionado (String).
 * @param onScheduleReminder Lambda que se invoca para programar el recordatorio.
 * @param modifier           Modificador Compose opcional.
 */
@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lista de posibles intervalos de recordatorio, incluyendo la duración en milisegundos y la unidad de tiempo.
    val reminders = listOf(
        Reminder(R.string.five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, plantName),
        Reminder(R.string.one_day, ONE_DAY, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_week, SEVEN_DAYS, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_month, THIRTY_DAYS, TimeUnit.DAYS, plantName)
    )

    // AlertDialog para mostrar opciones de recordatorio al usuario.
    AlertDialog(
        onDismissRequest = { onDialogDismiss() }, // Se invoca cuando el usuario toca fuera del diálogo o presiona el botón Atrás.
        confirmButton = {}, // No hay botón de confirmación separado, ya que la selección se hace al tocar una opción de texto.
        title = { Text(stringResource(R.string.remind_me, plantName)) }, // Título del diálogo, personalizado con el nombre de la planta.
        text = { // Contenido principal del diálogo.
            Column { // Una columna para organizar las opciones de recordatorio verticalmente.
                // Itera sobre cada opción de `reminder` y crea un `Text` clickeable para cada una.
                reminders.forEach { reminder ->
                    Text(
                        text = stringResource(reminder.durationRes), // Texto que muestra la duración (e.g., "5 seconds", "1 day").
                        modifier = Modifier
                            .clickable {
                                // Al hacer clic en una opción:
                                onScheduleReminder(reminder) // Llama a la lambda para programar el recordatorio.
                                onDialogDismiss() // Cierra el diálogo después de la selección.
                            }
                            .fillMaxWidth() // Ocupa el ancho completo para facilitar el clic.
                            .padding(vertical = 8.dp) // Padding vertical para cada opción de texto.
                    )
                }
            }
        },
        modifier = modifier // Aplica el modificador externo al AlertDialog.
    )
}

// Previews para Android Studio. Permiten ver cómo se renderizan los Composable sin ejecutar la aplicación en un emulador o dispositivo.
@Preview(showBackground = true) // Muestra el fondo para una mejor visibilidad en el preview.
@Composable
fun PlantListItemPreview() {
    WaterMeTheme { // Aplica el tema para asegurar que el preview se vea como en la aplicación real.
        PlantListItem(DataSource.plants[0], {}) // Muestra el primer elemento de la lista de plantas.
    }
}

@Preview(showBackground = true)
@Composable
fun PlantListContentPreview() {
    PlantListContent(plants = DataSource.plants, onScheduleReminder = {}) // Muestra la lista completa de plantas.
}