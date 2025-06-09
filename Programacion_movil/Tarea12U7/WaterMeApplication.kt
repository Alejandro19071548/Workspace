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

package com.example.waterme

// Importa la clase Application de Android
import android.app.Application
import com.example.waterme.data.AppContainer
import com.example.waterme.data.DefaultAppContainer

/**
 * WaterMeApplication: Clase que extiende Application para inicializar dependencias.
 *
 * Esta clase es el punto de entrada de la aplicación a nivel de proceso. Se inicializa
 * antes que cualquier Activity o Service, lo que la convierte en un lugar ideal para
 * configurar el contenedor de inyección de dependencias y otras inicializaciones globales.
 */
class WaterMeApplication : Application() {
    /** Instancia de AppContainer que proporciona dependencias (repositorio, etc.) */
    // `lateinit` indica que la variable será inicializada más tarde, garantizando que
    // no será nula cuando se acceda a ella, evitando la necesidad de un tipo nullable.
    lateinit var container: AppContainer

    /**
     * onCreate: Se llama cuando el sistema Android crea la aplicación.
     *
     * Este método es el primer callback que se llama cuando se crea el proceso de la aplicación.
     * Es ideal para realizar inicializaciones que necesitan estar disponibles globalmente.
     */
    override fun onCreate() {
        super.onCreate() // Llama al método onCreate de la superclase Application.
        // Inicializa el contenedor de dependencias al iniciar la aplicación.
        // `DefaultAppContainer` es la implementación concreta del contenedor que se encarga
        // de proporcionar instancias de las dependencias, como `WaterRepository`.
        container = DefaultAppContainer(this)
    }
}
