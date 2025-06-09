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

package com.example.waterme.worker

// Importa clases necesarias para WorkManager y Context
import android.content.Context // Contexto de la aplicación.
import androidx.work.CoroutineWorker // Clase base para un Worker que puede ejecutar trabajo asíncrono en una corrutina.
import androidx.work.WorkerParameters // Contiene los parámetros para configurar el Worker.
import com.example.waterme.R // Clase generada automáticamente para acceder a los recursos de la aplicación.

/**
 * WaterReminderWorker: Worker que envía una notificación cuando toca regar la planta.
 *
 * Este Worker se encarga de la tarea en segundo plano de mostrar una notificación
 * al usuario para recordarle que debe regar una planta.
 *
 * @param context      Contexto de la aplicación. Proporcionado por WorkManager.
 * @param workerParams Parámetros para configurar el Worker (incluye inputData). Proporcionado por WorkManager.
 */
class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) { // Hereda de CoroutineWorker para soportar operaciones asíncronas.

    /**
     * doWork: Método suspendido que se ejecuta en segundo plano.
     * Envía la notificación de recordatorio.
     *
     * Este es el método principal donde se realiza el trabajo del Worker.
     * Al ser `suspend`, puede ejecutar operaciones de larga duración sin bloquear el hilo principal.
     *
     * @return Resultado del trabajo: Result.success() si todo salió bien,
     * o Result.failure() o Result.retry() en caso de error.
     */
    override suspend fun doWork(): Result {
        // Obtiene el nombre de la planta desde los datos de entrada (inputData).
        // `inputData` es un `Data` object que puede contener información pasada al Worker.
        // `nameKey` es una constante definida en el `companion object` para asegurar consistencia.
        val plantName = inputData.getString(nameKey)

        // Llama a la función que crea y muestra la notificación.
        // Se obtiene el string del recurso `R.string.time_to_water` y se formatea con el nombre de la planta.
        makePlantReminderNotification(
            applicationContext.resources.getString(R.string.time_to_water, plantName),
            applicationContext // Se pasa el contexto de la aplicación para crear la notificación.
        )
        // Indica que el trabajo fue exitoso. Esto le dice a WorkManager que el Worker ha terminado su tarea.
        return Result.success()
    }

    /**
     * Companion object para definir constantes o métodos estáticos relacionados con `WaterReminderWorker`.
     */
    companion object {
        // Clave para extraer el nombre de planta de inputData.
        // Se usa para pasar el nombre de la planta desde el código que programa el Worker.
        const val nameKey = "NAME"
    }
}
