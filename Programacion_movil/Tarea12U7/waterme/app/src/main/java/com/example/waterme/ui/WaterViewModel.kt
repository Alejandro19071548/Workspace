/*
 * ViewModel que maneja la lógica de negocio para la aplicación WaterMe,
 * interactuando con el repositorio de datos WaterRepository para obtener
 * la lista de plantas y programar recordatorios de riego.
 */

package com.example.waterme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.waterme.WaterMeApplication
import com.example.waterme.data.Reminder
import com.example.waterme.data.WaterRepository

// ViewModel principal que expone la lista de plantas y permite programar recordatorios
class WaterViewModel(private val waterRepository: WaterRepository) : ViewModel() {

    // Lista observable de plantas obtenida del repositorio
    internal val plants = waterRepository.plants

    // Función para programar un recordatorio con la duración, unidad y nombre de la planta
    fun scheduleReminder(reminder: Reminder) {
        waterRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.plantName)
    }

    /**
     * Factory para crear instancias de WaterViewModel, inyectando la dependencia WaterRepository
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Obtener la instancia de WaterRepository desde el contenedor de dependencias de la aplicación
                val waterRepository =
                    (this[APPLICATION_KEY] as WaterMeApplication).container.waterRepository
                WaterViewModel(
                    waterRepository = waterRepository
                )
            }
        }
    }
}
