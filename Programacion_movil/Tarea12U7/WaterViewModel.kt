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

// Importa clases de ViewModel y fábrica de ViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.waterme.WaterMeApplication
import com.example.waterme.data.Reminder
import com.example.waterme.data.WaterRepository

/**
 * WaterViewModel: Contiene la lógica para obtener plantas y programar recordatorios.
 *
 * @property waterRepository Repositorio que maneja datos de plantas y recordatorios.
 */
class WaterViewModel(private val waterRepository: WaterRepository) : ViewModel() {

    // Exponemos la lista de plantas desde el repositorio
    // Esta propiedad es `internal` lo que significa que es visible dentro del mismo módulo.
    // Al ser un `val` (inmutable) y estar expuesta directamente desde el `waterRepository`,
    // cualquier cambio en la lista de plantas del repositorio se reflejará aquí.
    internal val plants = waterRepository.plants

    /**
     * scheduleReminder: Llama al repositorio para programar un recordatorio.
     *
     * Este método actúa como un puente entre la UI (a través de `WaterMeApp` o `PlantListContent`)
     * y la capa de datos (`WaterRepository`).
     *
     * @param reminder Objeto Reminder con datos de tiempo y nombre de planta.
     */
    fun scheduleReminder(reminder: Reminder) {
        // Delega la responsabilidad de programar el recordatorio al `waterRepository`.
        // Esto sigue el principio de "separación de preocupaciones".
        waterRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.plantName)
    }

    /**
     * Factory para crear instancias de WaterViewModel con WaterRepository inyectado.
     *
     * Companion objects en Kotlin son útiles para definir métodos y propiedades estáticas.
     * En este caso, `Factory` es una instancia `ViewModelProvider.Factory` que sabe cómo
     * construir un `WaterViewModel`.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            // `initializer` es un bloque que se ejecuta para crear la instancia del ViewModel.
            initializer {
                // Obtiene la instancia de `WaterRepository` desde el contenedor de dependencias
                // de la aplicación. `APPLICATION_KEY` es una clave para acceder a la aplicación
                // dentro del contexto del ViewModelProvider.
                val waterRepository =
                    (this[APPLICATION_KEY] as WaterMeApplication).container.waterRepository
                // Devuelve una nueva instancia de WaterViewModel, inyectando el `waterRepository`
                // obtenido. Esto demuestra la inyección de dependencias manual.
                WaterViewModel(
                    waterRepository = waterRepository
                )
            }
        }
    }
}
