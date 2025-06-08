package com.example.dessertrelease.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Repositorio para manejar las preferencias de usuario usando DataStore.
 *
 * @param dataStore Instancia de DataStore para almacenar preferencias
 */
// Repositorio que maneja las preferencias del usuario usando DataStore
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>  // Se inyecta DataStore como dependencia para acceder a las preferencias
) {
    // Objeto compañero que contiene constantes internas de la clase
    private companion object {
        // Clave que se usará para guardar/leer el tipo de layout (booleano)
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")

        // Etiqueta para los logs en caso de errores
        const val TAG = "UserPreferencesRepo"
    }

    /**
     * Flow que expone si el layout actual es lineal.
     *
     * - true  -> Layout lineal (lista vertical)
     * - false -> Layout de cuadrícula (grid)
     * - Valor por defecto: true (lineal)
     */
    val isLinearLayout: Flow<Boolean> = dataStore.data
        // Manejo de errores cuando se intenta leer las preferencias
        .catch { exception ->
            if (exception is IOException) {
                // Si ocurre un error de IO (por ejemplo, archivo corrupto), se muestra un error en el log
                Log.e(TAG, "Error reading preferences.", exception)

                // Se emiten preferencias vacías para evitar que el flujo falle
                emit(emptyPreferences())
            } else {
                // Si la excepción no es de tipo IO, se relanza para que sea gestionada por quien llama
                throw exception
            }
        }
        // Se transforman las preferencias en un valor booleano
        .map { preferences ->
            // Se obtiene el valor almacenado (true/false), o se usa true si no existe aún
            preferences[IS_LINEAR_LAYOUT] ?: true
        }

    /**
     * Función de suspensión que guarda el tipo de layout elegido por el usuario.
     *
     * @param isLinearLayout Si es true, el usuario eligió layout lineal; si es false, eligió grid.
     */
    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        // Edita las preferencias en DataStore
        dataStore.edit { preferences ->
            // Guarda el nuevo valor bajo la clave definida
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }
}
