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

// Función que crea y muestra una notificación para recordar regar las plantas
fun makePlantReminderNotification(
    message: String, // Mensaje que se mostrará en la notificación
    context: Context  // Contexto para acceder a recursos del sistema
) {
    // Solo crear el canal de notificación en dispositivos con Android Oreo (API 26) o superior
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH // Nivel de importancia alto para que la notificación llame la atención
        // Crear un canal de notificación con un ID, nombre y nivel de importancia
        val channel = NotificationChannel(
            CHANNEL_ID, // ID único del canal
            VERBOSE_NOTIFICATION_CHANNEL_NAME, // Nombre visible del canal
            importance
        )
        // Descripción del canal para que el usuario entienda para qué sirve
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        // Obtener el servicio de notificaciones para crear el canal
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // Registrar el canal en el sistema, si el administrador de notificaciones no es nulo
        notificationManager?.createNotificationChannel(channel)
    }

    // Crear un PendingIntent que se usará cuando el usuario toque la notificación
    val pendingIntent: PendingIntent = createPendingIntent(context)

    // Construir la notificación con sus atributos
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono pequeño de la notificación
        .setContentTitle(NOTIFICATION_TITLE) // Título de la notificación
        .setContentText(message) // Texto del mensaje (pasado por parámetro)
        .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad alta para mostrar la notificación en primer plano
        .setVibrate(LongArray(0)) // Desactivar la vibración (longitud 0)
        .setContentIntent(pendingIntent) // Acción al pulsar la notificación
        .setAutoCancel(true) // La notificación se cancela automáticamente al pulsarla

    // Mostrar la notificación usando el NotificationManagerCompat
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

// Función que crea un PendingIntent para abrir la MainActivity cuando el usuario pulse la notificación
fun createPendingIntent(appContext: Context): PendingIntent {
    // Crear un Intent para abrir MainActivity
    val intent = Intent(appContext, MainActivity::class.java).apply {
        // Configurar flags para que la actividad sea nueva y limpia la pila anterior
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Inicializar flags para el PendingIntent con FLAG_UPDATE_CURRENT para actualizar el intent si ya existe
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    // Si la versión de Android es Marshmallow (API 23) o superior, añadir FLAG_IMMUTABLE para mayor seguridad
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    // Crear y devolver el PendingIntent con el intent y flags configurados
    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE, // Código para identificar el PendingIntent
        intent,
        flags
    )
}
