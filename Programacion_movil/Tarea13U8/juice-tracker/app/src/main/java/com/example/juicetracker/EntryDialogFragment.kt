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
package com.example.juicetracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.R.layout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * This dialog allows the user to enter information about a donut, either creating a new
 * entry or updating an existing one.
 */
// Clase que representa un diálogo de tipo BottomSheet para ingresar o editar un jugo
class EntryDialogFragment : BottomSheetDialogFragment() {

    // Obtiene una instancia del ViewModel asociado a esta pantalla, usando un proveedor personalizado
    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }

    // Variable que guarda el color seleccionado por defecto (Rojo)
    var selectedColor: JuiceColor = JuiceColor.Red

    // Infla el layout asociado a este fragmento al crearse la vista
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEntryDialogBinding.inflate(inflater, container, false).root
    }

    // Configura los elementos del fragmento una vez creada la vista
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Mapa que asocia el texto del Spinner con su valor enumerado JuiceColor
        val colorLabelMap = JuiceColor.values().associateBy { getString(it.label) }

        // Se obtiene el binding para acceder a los elementos del layout
        val binding = FragmentEntryDialogBinding.bind(view)

        // Se obtienen los argumentos enviados al fragmento
        val args: EntryDialogFragmentArgs by navArgs()
        val juiceId = args.itemId

        // Si se recibió un ID válido (mayor a 0), se está editando un jugo existente
        if (args.itemId > 0) {
            // Se recupera el jugo desde la base de datos y se llena la UI con sus datos
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    entryViewModel.getJuiceStream(args.itemId).filterNotNull().collect { item ->
                        with(binding){
                            name.setText(item.name)
                            description.setText(item.description)
                            ratingBar.rating = item.rating.toFloat()
                            colorSpinner.setSelection(findColorIndex(item.color))
                        }
                    }
                }
            }
        }

        // Habilita el botón de Guardar solo si hay texto ingresado
        binding.name.doOnTextChanged { _, start, _, count ->
            binding.saveButton.isEnabled = (start + count) > 0
        }

        // Configura el Spinner con los nombres de colores disponibles
        binding.colorSpinner.adapter = ArrayAdapter(
            requireContext(),
            layout.support_simple_spinner_dropdown_item,
            colorLabelMap.map { it.key }
        )

        // Manejador para detectar cuando el usuario selecciona un color
        binding.colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val selected = parent.getItemAtPosition(pos).toString()
                selectedColor = colorLabelMap[selected] ?: selectedColor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedColor = JuiceColor.Red // Valor por defecto
            }
        }

        // Al hacer clic en Guardar, se guardan los datos en el ViewModel y se cierra el diálogo
        binding.saveButton.setOnClickListener {
            entryViewModel.saveJuice(
                juiceId,
                binding.name.text.toString(),
                binding.description.text.toString(),
                selectedColor.name,
                binding.ratingBar.rating.toInt()
            )
            dismiss()
        }

        // Botón Cancelar: simplemente cierra el diálogo sin guardar cambios
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    // Función auxiliar que busca la posición de un color dentro del enumerado JuiceColor
    private fun findColorIndex(color: String): Int {
        val juiceColor = JuiceColor.valueOf(color)
        return JuiceColor.values().indexOf(juiceColor)
    }
}
