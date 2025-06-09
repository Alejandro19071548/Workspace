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

package com.example.waterme

// Constantes para el canal de notificaciones y valores relacionados con las notificaciones y recordatorios

// Nombre visible del canal de notificaciones para notificaciones detalladas (verbose)
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"

// Descripción del canal de notificaciones para que el usuario entienda su propósito
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"

// Título estándar para las notificaciones que se mostrarán (ejemplo: "¡Water me!")
val NOTIFICATION_TITLE: CharSequence = "Water me!"

// ID único para el canal de notificaciones, usado para crear y referenciar el canal
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"

// ID único para la notificación, útil para actualizar o cancelar la notificación
const val NOTIFICATION_ID = 1

// Código de solicitud para PendingIntent, usado para identificar la intención pendiente
const val REQUEST_CODE = 0

// Constantes para programar recordatorios o temporizadores, en unidades no especificadas aquí
// (pueden representar días o segundos, según contexto de uso)

// 5 segundos (usado para pruebas o recordatorios muy frecuentes)
const val FIVE_SECONDS: Long = 5

// 1 día (posiblemente usado para recordatorios diarios)
const val ONE_DAY: Long = 1

// 7 días (una semana, para recordatorios semanales)
const val SEVEN_DAYS: Long = 7

// 30 días (para recordatorios mensuales o eventos a largo plazo)
const val THIRTY_DAYS: Long = 30
