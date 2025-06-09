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

// Importaciones necesarias para el uso de Application y clases de contenedor de datos
import android.app.Application
import com.example.bluromatic.data.AppContainer
import com.example.bluromatic.data.DefaultAppContainer

/**
 * Clase de aplicación personalizada para Bluromatic.
 * Se utiliza para inicializar objetos globales, como el contenedor de dependencias.
 */
class BluromaticApplication : Application()  {

    /**
     * Instancia del contenedor de la aplicación.
     * Se utiliza por otras clases para acceder a las dependencias compartidas.
     */
    lateinit var container: AppContainer

    /**
     * Método que se llama cuando se crea la aplicación por primera vez.
     * Aquí se inicializa el contenedor de dependencias.
     */
    override fun onCreate() {
        super.onCreate()
        // Se asigna una implementación por defecto del contenedor con el contexto de la app
        container = DefaultAppContainer(this)
    }
}
