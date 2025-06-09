/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.reply.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reply.data.Email
import com.example.reply.data.MailboxType
import com.example.reply.ui.utils.ReplyNavigationType
@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass, // Parámetro que indica el tamaño de la ventana (compacto, mediano o expandido)
    modifier: Modifier = Modifier, // Modificador opcional para personalización del componente
) {
    // Variable para definir el tipo de navegación según el tamaño de la pantalla
    val navigationType: ReplyNavigationType

    // Se obtiene una instancia del ViewModel
    val viewModel: ReplyViewModel = viewModel()

    // Se observa el estado de la UI proveniente del ViewModel
    val replyUiState = viewModel.uiState.collectAsState().value

    // Se determina el tipo de navegación según el tamaño de ventana
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION // Navegación inferior para pantallas pequeñas
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = ReplyNavigationType.NAVIGATION_RAIL // Rail de navegación lateral para pantallas medianas
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER // Drawer fijo para pantallas grandes
        }
        else -> {
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION // Valor por defecto
        }
    }

    // Se muestra la pantalla principal de la app con los parámetros correspondientes
    ReplyHomeScreen(
        navigationType = navigationType, // Tipo de navegación definido arriba
        replyUiState = replyUiState, // Estado actual de la interfaz
        onTabPressed = { mailboxType: MailboxType ->
            viewModel.updateCurrentMailbox(mailboxType = mailboxType) // Actualiza el buzón activo
            viewModel.resetHomeScreenStates() // Reinicia estados relacionados a la pantalla de inicio
        },
        onEmailCardPressed = { email: Email ->
            viewModel.updateDetailsScreenStates(
                email = email // Muestra el detalle del email seleccionado
            )
        },
        onDetailScreenBackPressed = {
            viewModel.resetHomeScreenStates() // Acción al presionar "atrás" en detalles
        },
        modifier = modifier // Modificador opcional para el diseño
    )
}
