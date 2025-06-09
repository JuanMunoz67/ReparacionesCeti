package com.example.reparacionesceti

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.Reporte
import kotlinx.coroutines.launch

class NuevoReporteFragment : Fragment() {

    private lateinit var etTitulo: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var imgPreview: ImageView

    private lateinit var db: AppDatabase

    private var imagenUri: Uri? = null

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imagenUri = data?.data
            if (imagenUri != null) {
                imgPreview.setImageURI(imagenUri)
                imgPreview.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nuevo_reporte, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etTitulo = view.findViewById(R.id.etTitulo)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen)
        imgPreview = view.findViewById(R.id.imgPreview)

        db = AppDatabase.getDatabase(requireContext())

        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            seleccionarImagenLauncher.launch(intent)
        }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val ubicacion = etUbicacion.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            if (titulo.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoReporte = Reporte(
                titulo = titulo,
                ubicacion = ubicacion,
                descripcion = descripcion,
                estado = "Pendiente",
                imagenUri = imagenUri?.toString(),
                fecha = System.currentTimeMillis()
            )

            lifecycleScope.launch {
                db.reporteDao().insertar(nuevoReporte)
                Toast.makeText(requireContext(), "Reporte enviado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
        }
    }

    private fun limpiarCampos() {
        etTitulo.text.clear()
        etUbicacion.text.clear()
        etDescripcion.text.clear()
        imagenUri = null
        imgPreview.setImageURI(null)
        imgPreview.visibility = View.GONE
    }
}
