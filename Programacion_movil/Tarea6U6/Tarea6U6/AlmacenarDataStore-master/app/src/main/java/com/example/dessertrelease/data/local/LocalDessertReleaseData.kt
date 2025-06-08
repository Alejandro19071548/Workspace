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
package com.example.dessertrelease.data.local

/**
 * Objeto que contiene los nombres de las versiones de postres de Android.
 *
 * Proporciona una lista inmutable y ordenada cronológicamente con los nombres
 * en clave de postres utilizados para las versiones principales del sistema Android.
 */
object LocalDessertReleaseData {
    /**
     * Lista inmutable de nombres de postres usados en releases de Android.
     *
     * La lista sigue el orden cronológico de lanzamientos y contiene:
     * - Nombres únicos (no repetidos)
     * - La secuencia histórica completa desde versiones antiguas hasta modernas
     * - Incluye tanto nombres tradicionales como algunos añadidos recientemente
     */
    val dessertReleases = listOf(
        "Cupcake",              // Android 1.5 (2009)
        "Donut",                // Android 1.6 (2009)
        "Eclair",               // Android 2.0-2.1 (2009-2010)
        "Froyo",                // Android 2.2 (2010)
        "Gingerbread",          // Android 2.3 (2010-2011)
        "Honeycomb",            // Android 3.0 (2011) - Versión tablet
        "Ice Cream Sandwich",   // Android 4.0 (2011)
        "Jelly Bean",           // Android 4.1-4.3 (2012-2013)
        "KitKat",               // Android 4.4 (2013) - Primera marca comercial
        "Lollipop",             // Android 5.0 (2014)
        "Marshmallow",          // Android 6.0 (2015)
        "Nougat",               // Android 7.0 (2016)
        "Oreo",                 // Android 8.0 (2017)
        "Pie",                  // Android 9.0 (2018)
        "Quince Tart",          // Android 10 (2019) - Cambio de nomenclatura
        "Red Velvet Cake",      // Android 11 (2020)
        "Snow Cone",            // Android 12 (2021)
        "Tiramisu"              // Android 13 (2022)
    )
}