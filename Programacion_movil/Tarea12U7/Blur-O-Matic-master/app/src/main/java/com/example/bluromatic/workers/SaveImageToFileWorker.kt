/*
 * Copyright (C) 2023 The Android Open Source Project
 * Licencia Apache 2.0
 * Puedes usar este archivo solo si cumples con los términos de la licencia.
 * Proporcionado "tal cual", sin garantías.
 */

package com.example.bluromatic.workers

// Importaciones necesarias
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.*
import com.example.bluromatic.domain.CreateImageUriUseCase
import com.example.bluromatic.domain.SaveImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Etiqueta para logs
private const val TAG = "SaveImageToFileWorker"

/**
 * Worker de tipo CoroutineWorker encargado de guardar una imagen en almacenamiento externo.
 */
class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    // Título que se le asignará a la imagen guardada
    private val title = "Blurred Image"

    // Formato de fecha usado en el nombre del archivo
    private val dateFormatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())

    /**
     * Función que se ejecuta en segundo plano cuando se activa este Worker.
     */
    override suspend fun doWork(): Result {
        // Muestra una notificación indicando que se está guardando la imagen
        makeStatusNotification(
            applicationContext.resources.getString(R.string.saving_image),
            applicationContext
        )

        // Ejecutar la operación en un contexto de I/O
        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS) // Retraso simulado para emular procesamiento

            val resolver = applicationContext.contentResolver

            return@withContext try {
                // Obtener URI de entrada desde los datos del Worker
                val resourceUri = inputData.getString(KEY_IMAGE_URI)

                // Decodificar el bitmap desde el URI proporcionado
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                val imageUrl: String?

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ usa scoped storage y requiere crear URI y escribir manualmente
                    val createImageUriUseCase = CreateImageUriUseCase()
                    val saveImageUseCase = SaveImageUseCase()

                    // Crear un nuevo URI para guardar la imagen
                    val imageUri = createImageUriUseCase(
                        resolver,
                        "${title}_${dateFormatter.format(Date())}.jpg"
                    )

                    // Guardar la imagen en el nuevo URI
                    saveImageUseCase(resolver, imageUri, bitmap)

                    imageUrl = imageUri.toString() // Convertir a string para devolver
                } else {
                    // Versiones antiguas usan MediaStore directamente (ya no recomendado)
                    @Suppress("DEPRECATION")
                    imageUrl = MediaStore.Images.Media.insertImage(
                        resolver,
                        bitmap,
                        title,
                        dateFormatter.format(Date())
                    )
                }

                // Validar si se guardó correctamente y devolver éxito con datos
                if (!imageUrl.isNullOrEmpty()) {
                    val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                    Result.success(output)
                } else {
                    Log.e(TAG, applicationContext.resources.getString(R.string.writing_to_mediaStore_failed))
                    Result.failure()
                }

            } catch (exception: Exception) {
                // En caso de error, registrar el mensaje y devolver fallo
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_saving_image),
                    exception
                )
                Result.failure()
            }
        }
    }
}
