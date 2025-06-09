/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.waterme.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.waterme.data.Reminder
import com.example.waterme.model.Plant
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waterme.ui.theme.WaterMeTheme
import com.example.waterme.FIVE_SECONDS
import com.example.waterme.ONE_DAY
import com.example.waterme.R
import com.example.waterme.SEVEN_DAYS
import com.example.waterme.THIRTY_DAYS
import com.example.waterme.data.DataSource
import androidx.compose.ui.tooling.preview.Preview
import java.util.concurrent.TimeUnit
// Función principal de la app que usa Jetpack Compose
@Composable
fun WaterMeApp(waterViewModel: WaterViewModel = viewModel(factory = WaterViewModel.Factory)) {
    // Detecta la dirección del layout (izquierda a derecha o viceversa)
    val layoutDirection = LocalLayoutDirection.current

    // Aplica el tema personalizado
    WaterMeTheme {
        // Superficie principal que ocupa toda la pantalla
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    // Se agregan márgenes seguros para evitar superposición con barras del sistema
                    start = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateEndPadding(layoutDirection)
                ),
        ) {
            // Muestra la lista de plantas
            PlantListContent(
                plants = waterViewModel.plants,
                onScheduleReminder = { waterViewModel.scheduleReminder(it) }
            )
        }
    }
}

// Función que muestra el contenido de la lista de plantas
@Composable
fun PlantListContent(
    plants: List<Plant>,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    // Guarda la planta seleccionada
    var selectedPlant by rememberSaveable { mutableStateOf(plants[0]) }

    // Controla la visibilidad del diálogo de recordatorio
    var showReminderDialog by rememberSaveable { mutableStateOf(false) }

    // Lista vertical de elementos (LazyColumn)
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        // Recorre la lista de plantas y muestra cada una como ítem
        items(items = plants) {
            PlantListItem(
                plant = it,
                onItemSelect = { plant ->
                    selectedPlant = plant
                    showReminderDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Si se seleccionó una planta, se muestra el diálogo de recordatorio
    if (showReminderDialog) {
        ReminderDialogContent(
            onDialogDismiss = { showReminderDialog = false },
            plantName = stringResource(selectedPlant.name),
            onScheduleReminder = onScheduleReminder
        )
    }
}

// Función que representa cada ítem de planta en la lista
@Composable
fun PlantListItem(plant: Plant, onItemSelect: (Plant) -> Unit, modifier: Modifier = Modifier) {
    // Cada ítem se presenta dentro de una tarjeta (Card) clickeable
    Card(modifier = modifier
        .clickable { onItemSelect(plant) }
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(plant.name),
                modifier = Modifier.fillMaxWidth(),
                style = typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(text = stringResource(plant.type), style = typography.titleMedium)
            Text(text = stringResource(plant.description), style = typography.titleMedium)
            Text(
                text = "${stringResource(R.string.water)} ${stringResource(plant.schedule)}",
                style = typography.titleMedium
            )
        }
    }
}

// Diálogo para seleccionar un tiempo de recordatorio
@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lista de opciones de recordatorio
    val reminders = listOf(
        Reminder(R.string.five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, plantName),
        Reminder(R.string.one_day, ONE_DAY, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_week, SEVEN_DAYS, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_month, THIRTY_DAYS, TimeUnit.DAYS, plantName)
    )

    // Cuadro de diálogo
    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        confirmButton = {}, // No hay botón de confirmar porque el clic ya ejecuta
        title = { Text(stringResource(R.string.remind_me, plantName)) },
        text = {
            Column {
                // Cada opción es un botón de texto
                reminders.forEach {
                    Text(
                        text = stringResource(it.durationRes),
                        modifier = Modifier
                            .clickable {
                                onScheduleReminder(it)
                                onDialogDismiss()
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}

// Vista previa de un ítem de planta
@Preview(showBackground = true)
@Composable
fun PlantListItemPreview() {
    WaterMeTheme {
        PlantListItem(DataSource.plants[0], {})
    }
}

// Vista previa de la lista de plantas
@Preview(showBackground = true)
@Composable
fun PlantListContentPreview() {
    PlantListContent(plants = DataSource.plants, onScheduleReminder = {})
}
