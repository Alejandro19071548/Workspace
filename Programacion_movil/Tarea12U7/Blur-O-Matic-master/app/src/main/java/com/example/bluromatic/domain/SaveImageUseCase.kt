package com.example.bluromatic.domain

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "SaveImage"

/**
 * Caso de uso para guardar una imagen en MediaStore.
 *
 * @param resolver ContentResolver para acceder al almacenamiento.
 * @param contentUri URI donde se guardará la imagen (debe ser un Uri válido y creado previamente).
 * @param bitmap Bitmap de la imagen que queremos guardar.
 *
 * Esta función es `suspend` y se ejecuta en un contexto IO para no bloquear el hilo principal.
 */
class SaveImageUseCase {
    suspend operator fun invoke(
        resolver: ContentResolver,
        contentUri: Uri?,
        bitmap: Bitmap,
    ) = withContext(Dispatchers.IO) {
        try {
            contentUri?.let { contentUri ->
                // Abrimos un OutputStream para escribir la imagen en la URI dada
                resolver.openOutputStream(contentUri, "w")?.use { outputStream ->
                    // Comprimimos el bitmap en formato JPEG con calidad 85 y lo escribimos en el outputStream
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                }
                // Actualizamos el MediaStore para marcar que la imagen ya no está pendiente (IS_PENDING = 0)
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
                resolver.update(contentUri, values, null, null)
            }
        } catch (e: Throwable) {
            // Logueamos cualquier error que ocurra durante el guardado
            Log.e(TAG, "Error occurs $e")
            // Re-lanzamos la excepción para manejarla en la capa superior
            throw e
        }
    }
}
