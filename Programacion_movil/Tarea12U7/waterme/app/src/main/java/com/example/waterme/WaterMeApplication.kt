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

import android.app.Application
import com.example.waterme.data.AppContainer
import com.example.waterme.data.DefaultAppContainer

// Clase que extiende Application, usada para inicializar componentes globales cuando la app arranca
class WaterMeApplication : Application() {

    /**
     * Instancia del contenedor de dependencias (AppContainer) usada por otras clases
     * para acceder a servicios, repositorios y otros objetos compartidos
     */
    lateinit var container: AppContainer

    // Método que se llama cuando la aplicación se crea
    override fun onCreate() {
        super.onCreate()
        // Inicializa el contenedor con una implementación por defecto,
        // pasando el contexto de la aplicación para poder crear las dependencias necesarias
        container = DefaultAppContainer(this)
    }
}
