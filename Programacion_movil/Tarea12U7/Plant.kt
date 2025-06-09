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

package com.example.waterme.model

// Importa Parcelable y anotaciones para recursos de strings
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

/**
 * Data class que representa la información de una planta:
 * - name: recurso de string para el nombre
 * - type: recurso de string para el tipo
 * - description: recurso de string para la descripción
 * - schedule: recurso de string para el intervalo de riego
 *
 * Implementa Parcelable para poder pasarse entre componentes de Android.
 * `Parcelize` es una anotación de Kotlin que genera automáticamente el código
 * necesario para la implementación de `Parcelable`, reduciendo el boilerplate.
 */
@Parcelize
data class Plant(
    // `@StringRes` es una anotación que indica que el valor de este parámetro
    // debe ser una referencia a un recurso de string (por ejemplo, `R.string.plant_name`).
    // Esto es útil para la seguridad de tipos y para que el IDE advierta si se pasa un entero que no es un recurso de string.
    @StringRes val name: Int,
    @StringRes val type: Int,
    @StringRes val description: Int,
    @StringRes val schedule: Int // Define la frecuencia de riego.
) : Parcelable // La interfaz `Parcelable` permite que objetos de esta clase
               // sean serializados y deserializados de manera eficiente para
               // pasarlos a través de Intents o Bundles entre componentes de Android.
