package com.example.bluromatic.workers

// Importaciones necesarias para notificaciones, manejo de imágenes, archivos y logs
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bluromatic.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

// Constante usada para identificar los logs de este archivo
private const val TAG = "WorkerUtils"

/**
 * Crea y muestra una notificación para indicar el estado de un trabajo en segundo plano.
 *
 * @param message El mensaje a mostrar en la notificación.
 * @param context El contexto necesario para acceder al sistema de notificaciones.
 */
fun makeStatusNotification(message: String, context: Context) {
    // Si el dispositivo tiene Android 8.0 o superior, se debe crear un canal de notificaciones
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME // Nombre del canal
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION // Descripción del canal
        val importance = NotificationManager.IMPORTANCE_HIGH // Alta prioridad
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
        }

        // Obtener el sistema de notificaciones y registrar el canal
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    // Construcción y publicación de la notificación
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono pequeño
        .setContentTitle(NOTIFICATION_TITLE) // Título de la notificación
        .setContentText(message) // Mensaje recibido por parámetro
        .setPriority(NotificationCompat.PRIORITY_HIGH) // Alta prioridad
        .setVibrate(LongArray(0)) // Sin vibración

    // Mostrar la notificación
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

/**
 * Aplica un efecto de desenfoque (blur) a un Bitmap.
 * Esta función es una simulación simple de blur usando escalado de imagen.
 *
 * @param bitmap El bitmap original.
 * @param blurLevel El nivel de desenfoque (entre más alto, más borroso).
 * @return El bitmap desenfocado.
 */
@WorkerThread
fun blurBitmap(bitmap: Bitmap, blurLevel: Int): Bitmap {
    // Escala hacia abajo el bitmap proporcionalmente al nivel de blur
    val input = Bitmap.createScaledBitmap(
        bitmap,
        bitmap.width / (blurLevel * 5),
        bitmap.height / (blurLevel * 5),
        true
    )

    // Luego lo escala de nuevo a su tamaño original, lo cual genera un efecto de blur
    return Bitmap.createScaledBitmap(input, bitmap.width, bitmap.height, true)
}

/**
 * Escribe un Bitmap a un archivo en el almacenamiento interno de la app.
 *
 * @param applicationContext Contexto de la aplicación.
 * @param bitmap El bitmap a guardar.
 * @return URI del archivo de imagen guardado.
 * @throws FileNotFoundException si el archivo no puede ser creado.
 */
@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    // Generar un nombre de archivo único
    val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())

    // Crear (si no existe) un directorio interno para las salidas del filtro
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs() // Crea el directorio
    }

    // Crear el archivo de salida
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null

    try {
        // Escribir el bitmap en formato PNG
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    } finally {
        // Asegurarse de cerrar el stream
        out?.let {
            try {
                it.close()
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString()) // Loguear el error si ocurre al cerrar
            }
        }
    }

    // Devolver la URI del archivo creado
    return Uri.fromFile(outputFile)
}
