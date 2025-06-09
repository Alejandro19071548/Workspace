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

package com.example.bluromatic

// Constantes del canal de notificaciones

// Nombre del canal de notificaciones para notificaciones detalladas del trabajo en segundo plano
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"

/*
 * Descripción del canal de notificaciones.
 * Se muestra cuando se inicia un trabajo en segundo plano.
 */
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"

// Título de la notificación que se muestra cuando inicia un WorkRequest
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"

// ID del canal de notificación utilizado por WorkManager
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"

// ID de la notificación; debe ser único para actualizar o cancelar notificaciones
const val NOTIFICATION_ID = 1

// Nombre utilizado para identificar el trabajo de manipulación de imágenes
const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

// Ruta de salida donde se almacenarán las imágenes procesadas
const val OUTPUT_PATH = "blur_filter_outputs"

// Clave usada para pasar el URI de la imagen entre trabajadores
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

// Etiqueta para identificar la salida de un trabajador
const val TAG_OUTPUT = "OUTPUT"

// Clave usada para indicar el nivel de desenfoque que se desea aplicar
const val KEY_BLUR_LEVEL = "KEY_BLUR_LEVEL"

// Tiempo de espera en milisegundos antes de ejecutar una tarea (3 segundos)
const val DELAY_TIME_MILLIS: Long = 3000
