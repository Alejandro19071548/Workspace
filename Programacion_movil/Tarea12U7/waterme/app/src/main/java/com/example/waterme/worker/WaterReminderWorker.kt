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

package com.example.waterme.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.waterme.R

// Clase que extiende CoroutineWorker para realizar un trabajo en segundo plano (background)
// Este worker enviará una notificación recordando regar una planta
class WaterReminderWorker(
    context: Context, // Contexto de la aplicación
    workerParams: WorkerParameters // Parámetros del trabajo (inputs, etc)
) : CoroutineWorker(context, workerParams) {

    // Método que se ejecuta cuando se inicia el trabajo en segundo plano
    override suspend fun doWork(): Result {

        // Obtener el nombre de la planta pasado en los datos de entrada con la clave "NAME"
        val plantName = inputData.getString(nameKey)

        // Llamar a la función que crea la notificación con un mensaje formateado
        // Se usa el recurso string "time_to_water" y se inserta el nombre de la planta
        makePlantReminderNotification(
            applicationContext.resources.getString(R.string.time_to_water, plantName),
            applicationContext
        )

        // Indicar que el trabajo terminó correctamente
        return Result.success()
    }

    companion object {
        // Constante para la clave del nombre de la planta en los datos de entrada
        const val nameKey = "NAME"
    }
}
