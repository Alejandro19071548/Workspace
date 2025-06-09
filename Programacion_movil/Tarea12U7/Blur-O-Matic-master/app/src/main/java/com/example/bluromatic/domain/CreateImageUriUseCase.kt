package com.example.bluromatic.domain

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso para crear un URI para guardar una imagen en MediaStore.
 *
 * @param resolver ContentResolver para acceder al almacenamiento.
 * @param filename Nombre del archivo que se desea crear.
 * @return Uri donde se podrá guardar la imagen, o null si falla.
 *
 * Esta función es `suspend` y se ejecuta en un contexto IO para no bloquear el hilo principal.
 */
@RequiresApi(29)
class CreateImageUriUseCase {
    suspend operator fun invoke(resolver: ContentResolver, filename: String): Uri? {
        return withContext(Dispatchers.IO) {
            // Se obtiene la URI base para imágenes en almacenamiento externo primario
            val imageCollection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            // Valores para describir la imagen que se creará
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)              // Nombre del archivo
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Blur-O-Matic") // Carpeta dentro de "Pictures"
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")              // Tipo MIME de la imagen
                put(MediaStore.Images.Media.IS_PENDING, 1)                        // Marca el archivo como pendiente (no visible todavía)
            }

            // Se inserta la entrada en MediaStore, obteniendo el Uri donde se podrá escribir la imagen
            val imageUri = resolver.insert(imageCollection, values)
            imageUri
        }
    }
}
