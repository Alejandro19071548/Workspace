package com.example.bluromatic.workers

// Importaciones necesarias
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.OUTPUT_PATH
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

// Etiqueta para logs
private const val TAG = "CleanupWorker"

/**
 * Worker encargado de eliminar los archivos temporales generados por el filtro blur.
 */
class CleanupWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    /**
     * Método principal que se ejecuta cuando el Worker comienza.
     */
    override suspend fun doWork(): Result {
        // Muestra una notificación indicando que se están limpiando archivos
        makeStatusNotification(
            applicationContext.resources.getString(R.string.cleaning_up_files),
            applicationContext
        )

        // Ejecuta la limpieza en el dispatcher de I/O
        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS) // Simula un retraso antes de empezar

            return@withContext try {
                // Directorio donde se almacenan los archivos temporales de salida
                val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)

                if (outputDirectory.exists()) {
                    val entries = outputDirectory.listFiles()
                    if (entries != null) {
                        // Recorre cada archivo en el directorio
                        for (entry in entries) {
                            val name = entry.name
                            // Verifica que el archivo sea una imagen PNG
                            if (name.isNotEmpty() && name.endsWith(".png")) {
                                // Intenta eliminar el archivo
                                val deleted = entry.delete()
                                Log.i(TAG, "Deleted $name - $deleted")
                            }
                        }
                    }
                }

                // Si todo va bien, retorna éxito
                Result.success()
            } catch (exception: Exception) {
                // Si ocurre un error, se registra y se retorna fallo
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_cleaning_file),
                    exception
                )
                Result.failure()
            }
        }
    }
}
