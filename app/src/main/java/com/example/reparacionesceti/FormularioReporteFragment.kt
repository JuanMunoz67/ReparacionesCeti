package com.example.reparacionesceti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reparacionesceti.databinding.FragmentFormularioReporteBinding

class FormularioReporteFragment : Fragment() {

    private var _binding: FragmentFormularioReporteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormularioReporteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            // Aquí validamos y guardamos el reporte
            guardarReporte()
        }
    }

    private fun guardarReporte() {
        val titulo = binding.etTitulo.text.toString().trim()
        val ubicacion = binding.etUbicacion.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val estado = binding.etEstado.text.toString().trim()
        // Para imagen y fecha, luego añadimos

        if (titulo.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty() || estado.isEmpty()) {
            // Mostrar error simple
            binding.tvError.text = "Por favor, completa todos los campos."
            return
        }

        // Aquí guardaríamos el reporte en BD o enviamos a ViewModel
        // Por ahora, solo mostramos mensaje o navegamos atrás

        // TODO: Guardar en BD

        // Limpiar formulario y navegar atrás o mostrar mensaje
        binding.tvError.text = ""
        // Para ejemplo simple: cerrar fragmento
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
