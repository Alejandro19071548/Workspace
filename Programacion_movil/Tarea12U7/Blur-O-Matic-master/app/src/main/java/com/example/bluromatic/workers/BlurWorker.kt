package com.example.bluromatic.workers

// Importaciones necesarias
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

// Constante para etiquetar los mensajes de log
private const val TAG = "BlurWorker"

/**
 * Worker encargado de aplicar un filtro de desenfoque (blur) a una imagen.
 */
class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    /**
     * Método principal que se ejecuta cuando comienza el trabajo.
     */
    override suspend fun doWork(): Result {
        // Obtiene la URI de la imagen de entrada desde los datos del Worker
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        // Obtiene el nivel de desenfoque solicitado (por defecto 1)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        // Muestra una notificación de que se está aplicando el blur
        makeStatusNotification(
            applicationContext.resources.getString(R.string.blurring_image),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            // Simula un pequeño retraso antes de empezar
            delay(DELAY_TIME_MILLIS)

            return@withContext try {
                // Asegura que la URI no esté vacía o nula
                require(!resourceUri.isNullOrBlank()) {
                    val errorMessage = applicationContext.resources.getString(R.string.invalid_input_uri)
                    Log.e(TAG, errorMessage)
                    errorMessage
                }

                // Carga la imagen desde la URI como un objeto Bitmap
                val resolver = applicationContext.contentResolver
                val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

                // Aplica el filtro de desenfoque con el nivel especificado
                val output = blurBitmap(picture, blurLevel)

                // Guarda el Bitmap resultante en un archivo y obtiene su URI
                val outputUri = writeBitmapToFile(applicationContext, output)

                // Devuelve los datos de salida con la nueva URI
                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
                Result.success(outputData)

            } catch (throwable: Throwable) {
                // Si ocurre algún error, lo registra y retorna fallo
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_applying_blur),
                    throwable
                )
                Result.failure()
            }
        }
    }
}
