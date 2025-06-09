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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.waterme.ui.WaterMeApp
import com.example.waterme.ui.theme.WaterMeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
       enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            // Aplica el tema personalizado WaterMeTheme a toda la UI
            WaterMeTheme {
                // Contenedor Surface que pinta un fondo usando el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(), // Ocupa todo el tamaño disponible
                    color = MaterialTheme.colorScheme.background // Color de fondo según el tema
                ) {

                    WaterMeApp()
                }
            }
        }
    }
}
