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

// Importa clases para notificaciones y Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.waterme.CHANNEL_ID
import com.example.waterme.MainActivity
import com.example.waterme.NOTIFICATION_ID
import com.example.waterme.NOTIFICATION_TITLE
import com.example.waterme.R
import com.example.waterme.REQUEST_CODE
import com.example.waterme.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.waterme.VERBOSE_NOTIFICATION_CHANNEL_NAME

/**
 * makePlantReminderNotification: Crea y envía una notificación de recordatorio para regar una planta.
 *
 * Esta función es responsable de la lógica de creación y visualización de una notificación de Android.
 * Se encarga de la compatibilidad con diferentes versiones de Android (canales de notificación).
 *
 * @param message Texto que se mostrará en la notificación.
 * @param context Contexto de la aplicación necesario para crear la notificación.
 */
fun makePlantReminderNotification(
    message: String,
    context: Context
) {
    // Solo crea canal en Android Oreo (API 26) o superior.
    // Los canales de notificación son obligatorios a partir de Android 8.0 (API 26)
    // para categorizar las notificaciones y permitir que los usuarios controlen su comportamiento.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Define importancia alta para el canal.
        // Una importancia alta significa que la notificación hará ruido y aparecerá como un "heads-up" (banner).
        val importance = NotificationManager.IMPORTANCE_HIGH
        // Crea el canal con ID, nombre y nivel de importancia.
        // `CHANNEL_ID`, `VERBOSE_NOTIFICATION_CHANNEL_NAME` son constantes definidas en otro lugar del proyecto.
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            importance
        )
        // Asigna descripción al canal. Esta descripción es visible para el usuario en la configuración de la aplicación.
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        // Obtiene el NotificationManager del sistema, que es el servicio encargado de gestionar las notificaciones.
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // Crea el canal en el sistema. Si el canal ya existe, no se hace nada.
        notificationManager?.createNotificationChannel(channel)
    }

    // Crea un PendingIntent que abre MainActivity al tocar la notificación.
    // Un PendingIntent es un token que se le da a una aplicación externa (en este caso, el sistema Android)
    // para que ejecute un Intent en tu nombre.
    val pendingIntent: PendingIntent = createPendingIntent(context)

    // Construye la notificación con ícono, títulos, prioridad, etc.
    // `NotificationCompat.Builder` es una clase de soporte para construir notificaciones compatibles con versiones antiguas de Android.
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono pequeño que se muestra en la barra de estado.
        .setContentTitle(NOTIFICATION_TITLE) // Título principal de la notificación.
        .setContentText(message) // Texto principal del contenido de la notificación.
        .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad para versiones anteriores a Android O.
        .setVibrate(LongArray(0)) // Sin vibración. Un arreglo vacío de Long desactiva la vibración por defecto.
        .setContentIntent(pendingIntent) // Asocia el PendingIntent que se ejecutará cuando el usuario toque la notificación.
        .setAutoCancel(true) // Cierra la notificación automáticamente cuando el usuario la toca.

    // Muestra la notificación usando NotificationManagerCompat.
    // `NotificationManagerCompat` proporciona una forma compatible de enviar notificaciones.
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

/**
 * createPendingIntent: Crea un PendingIntent para abrir MainActivity.
 *
 * @param appContext Contexto de la aplicación.
 * @return PendingIntent que lanza MainActivity.
 */
fun createPendingIntent(appContext: Context): PendingIntent {
    // Crea un Intent hacia MainActivity.
    // `Intent.FLAG_ACTIVITY_NEW_TASK` y `Intent.FLAG_ACTIVITY_CLEAR_TASK` se usan para
    // asegurar que la Activity se inicie en una nueva tarea y que cualquier tarea existente
    // que contenga la Activity sea eliminada.
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Define flags para detectar lanzamientos inseguros en Android 12+ (API 31+).
    // `PendingIntent.FLAG_UPDATE_CURRENT` si el PendingIntent ya existe, se actualizan sus datos.
    // `PendingIntent.FLAG_IMMUTABLE` es una bandera importante a partir de Android 12 (API 31)
    // para hacer que el PendingIntent sea inmutable, lo cual es una buena práctica de seguridad.
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    // Devuelve un PendingIntent que abrirá MainActivity.
    // `PendingIntent.getActivity` crea un PendingIntent que lanzará una Activity.
    return PendingIntent.getActivity(
        appContext, // Contexto.
        REQUEST_CODE, // Código de solicitud único para este PendingIntent.
        intent, // El Intent a ejecutar.
        flags // Las banderas de comportamiento.
    )
}
