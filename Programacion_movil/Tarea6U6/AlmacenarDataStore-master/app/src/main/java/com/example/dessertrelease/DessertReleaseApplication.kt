package com.example.dessertrelease

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dessertrelease.data.local.UserPreferencesRepository

// Nombre que se usará para identificar el archivo de preferencias en DataStore
private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"

// Extensión de Context para obtener un DataStore de tipo Preferences usando el nombre anterior
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME // Nombre del archivo de preferencias
)
/*
 * Punto de entrada personalizado para la app, útil para la inyección manual de dependencias
 */
class DessertReleaseApplication: Application() {

    // Repositorio que se usará en toda la app para manejar las preferencias del usuario
    lateinit var userPreferencesRepository: UserPreferencesRepository

    // Método que se ejecuta una vez cuando la aplicación se inicia
    override fun onCreate() {
        super.onCreate()

        // Inicializa el repositorio usando el DataStore creado anteriormente
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}