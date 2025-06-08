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
package com.example.dessertrelease.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dessertrelease.DessertReleaseApplication
import com.example.dessertrelease.R
import com.example.dessertrelease.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * ViewModel para gestionar el estado de la UI relacionado con la presentación (layout)
 * de los postres en la app Dessert Release.
 */
class DessertReleaseViewModel(
    private val userPreferencesRepository: UserPreferencesRepository // Repositorio para acceder y guardar preferencias del usuario
) : ViewModel() {

    // Exposición del estado de la UI como un StateFlow
    val uiState: StateFlow<DessertReleaseUiState> =
        // Se transforma el Flow isLinearLayout del repositorio en un objeto de estado de UI
        userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
            DessertReleaseUiState(isLinearLayout) // Se crea un objeto de estado de UI con el valor actual del layout
        }.stateIn(
            scope = viewModelScope, // El ciclo de vida del flujo está ligado al ViewModel
            // El flujo permanece activo mientras haya suscriptores durante 5 segundos después de la última suscripción
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = runBlocking { // Se obtiene un valor inicial de manera bloqueante (solo se ejecuta una vez al iniciar)
                DessertReleaseUiState(
                    isLinearLayout = userPreferencesRepository.isLinearLayout.first()
                    // Se obtiene el primer valor del flujo como valor inicial
                )
            }
        )

    /*
     * Método público que permite seleccionar el tipo de layout (lineal o de grilla)
     * y guarda la preferencia usando el repositorio.
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch { // Lanza una coroutine en el alcance del ViewModel
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
            // Guarda la preferencia del usuario en el DataStore
        }
    }

    companion object {
        // Fábrica personalizada para crear el ViewModel, usando los componentes de la app
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DessertReleaseApplication)
                // Se obtiene una referencia a la aplicación para acceder a sus dependencias
                DessertReleaseViewModel(application.userPreferencesRepository)
                // Se crea una instancia del ViewModel con el repositorio de preferencias
            }
        }
    }
}

/*
 * Clase de datos que representa el estado de la UI para la pantalla de postres
 */
data class DessertReleaseUiState(
    val isLinearLayout: Boolean = true, // Indica si el layout actual es lineal
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    // Devuelve el string adecuado dependiendo del layout actual (para accesibilidad)
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
    // Devuelve el ícono adecuado para mostrar en el botón de cambio de layout
)