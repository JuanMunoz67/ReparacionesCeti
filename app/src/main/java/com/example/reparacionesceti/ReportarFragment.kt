package com.example.reparacionesceti

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class ReportarFragment : Fragment() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var spUbicacion: Spinner
    private lateinit var imgPreview: ImageView
    private lateinit var btnSeleccionarFoto: Button
    private lateinit var btnEnviar: Button

    private var uriFoto: Uri? = null
    private val launcherFoto = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uriFoto = it
            imgPreview.visibility = View.VISIBLE
            imgPreview.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_reportar, container, false)

        etTitulo = vista.findViewById(R.id.etTitulo)
        etDescripcion = vista.findViewById(R.id.etDescripcion)
        spUbicacion = vista.findViewById(R.id.spUbicacion)
        imgPreview = vista.findViewById(R.id.imgPreview)
        btnSeleccionarFoto = vista.findViewById(R.id.btnSeleccionarFoto)
        btnEnviar = vista.findViewById(R.id.btnEnviar)

        // Ubicaciones del plantel
        val ubicaciones = arrayOf("Selecciona ubicación", "Baños", "Aulas", "Cafetería", "Oficinas", "Laboratorios")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ubicaciones)
        spUbicacion.adapter = adapter

        btnSeleccionarFoto.setOnClickListener {
            launcherFoto.launch("image/*")
        }

        btnEnviar.setOnClickListener {
            enviarReporte()
        }

        return vista
    }

    private fun enviarReporte() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val ubicacion = spUbicacion.selectedItem.toString()

        if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion == "Selecciona ubicación") {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí podrías guardar en Room, Firebase o enviar a un servidor
        Toast.makeText(requireContext(), "Reporte enviado correctamente", Toast.LENGTH_LONG).show()

        // Limpiar formulario
        etTitulo.text.clear()
        etDescripcion.text.clear()
        spUbicacion.setSelection(0)
        imgPreview.setImageDrawable(null)
        imgPreview.visibility = View.GONE
        uriFoto = null
    }
}
