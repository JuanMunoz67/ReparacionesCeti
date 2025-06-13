package com.example.reparacionesceti

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.dao.ReporteDao
import com.example.reparacionesceti.model.entities.Reporte
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
//import coil.load
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrearReporteActivity : AppCompatActivity() {
    private lateinit var imgView: ImageView
    private lateinit var etTitulo: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etNotas: EditText
    private lateinit var btnGuardar: Button
    private lateinit var chipGroupEstado: ChipGroup

    private var currentPhotoUri: Uri? = null

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    private lateinit var db: AppDatabase

    companion object {
        private const val EXTRA_REPORTE_ID = "reporteId"

    }

    private fun init() {
        imgView = findViewById(R.id.imageViewUpload)
        etTitulo = findViewById(R.id.etReporteTitulo)
        etUbicacion = findViewById(R.id.etReporteUbicacion)
        etDescripcion = findViewById(R.id.etReporteDescripcion)
        etNotas = findViewById(R.id.etReporteNotas)
        btnGuardar = findViewById(R.id.btnDoRegister)
        chipGroupEstado = findViewById(R.id.chipGroupReporteEstado)

        imgView.setOnClickListener{
            showImageSourceDialog()
        }

        btnGuardar.setOnClickListener {
            saveReport()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        db = AppDatabase.getDatabase(this)
    }

    private fun setValuesIfAny() {
        val reporteId = intent.getIntExtra(EXTRA_REPORTE_ID, -1)

        if (reporteId != -1) {
            lifecycleScope.launch {
                val reporte = db.reporteDao().obtenerPorId(reporteId)
                reporte?.let {
                    etTitulo.setText(it.titulo)
                    etUbicacion.setText(it.ubicacion)
                    etDescripcion.setText(it.descripcion)
                    etNotas.setText(it.notas)
                    chipGroupEstado.check(
                        when (it.estado) {
                            "Pendiente" -> R.id.chipPendiente
                            "En proceso" -> R.id.chipEnProceso
                            "Resuelto" -> R.id.chipResuelto
                            else -> -1
                        }
                    )
                    imgView.load(it.imagenUri) {
                        crossfade(true)
                        placeholder(R.drawable.baseline_add_a_photo_24)
                    }
                }

            }
        }
    }

    private fun saveReport() {
        val titulo = etTitulo.text.toString().trim()
        val ubicacion = etUbicacion.text.toString().trim()
        val descripcion = etDescripcion.text.toString()
        val notas = etNotas.text.toString()
        val imagenUri = currentPhotoUri.toString()
        val estadoId = chipGroupEstado.checkedChipId
        val estadoReporte = findViewById<Chip>(estadoId)?.text.toString()

        if (titulo.isNotEmpty() && ubicacion.isNotEmpty() && descripcion.isNotEmpty() && imagenUri.isNotEmpty() && (estadoReporte != "")) {
            val timeStamp: String = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(Date())
            val reporte = Reporte(
                titulo = titulo,
                ubicacion = ubicacion,
                descripcion = descripcion,
                notas = notas,
                estado = estadoReporte,
                imagenUri = imagenUri?.toString(),
                fecha = timeStamp.toString()
            )
            lifecycleScope.launch { db.reporteDao().insertar(reporte) }
        }
        else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_reporte)

        init()
        setupLaunchers()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setValuesIfAny()
    }

    private fun setupLaunchers() {
        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }

        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val readMediaImagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                val readExternalStorageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (readMediaImagesGranted) {
                        openGallery()
                    } else {
                        Toast.makeText(this, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
                    }
                } else { // Pre-Android 13
                    if (readExternalStorageGranted) {
                        openGallery()
                    } else {
                        Toast.makeText(this, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                //gView.setImageURI(currentPhotoUri)
                currentPhotoUri?.let { uri ->
                    imgView.load(uri) {
                        crossfade(true)
                        placeholder(R.drawable.baseline_add_a_photo_24) // Opcional: placeholder
                        //error(R.drawable.baseline_add_a_photo_24)
                    }
                }
            } else {
                Toast.makeText(this, "Error al tomar la foto.", Toast.LENGTH_SHORT).show()
                currentPhotoUri = null
            }
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                currentPhotoUri = it
                imgView.load(it) {
                    crossfade(true)
                    placeholder(R.drawable.baseline_add_a_photo_24)
                    //error(R.drawable.baseline_add_a_photo_24)
                }
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Tomar foto", "Elegir de la galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpenCamera()
                    1 -> checkGalleryPermissionAndOpenGallery()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkGalleryPermissionAndOpenGallery() {
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else { // Pre-Android 13
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        var allPermissionsGranted = true
        for (permission in permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
                break
            }
        }

        if (allPermissionsGranted) {
            openGallery()
        } else {
            requestGalleryPermissionLauncher.launch(permissionsToRequest)
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: Exception) {
            Toast.makeText(this, "Error al crear archivo de imagen", Toast.LENGTH_SHORT).show()
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                it
            )
            currentPhotoUri = photoURI
            takePictureLauncher.launch(photoURI)
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    @Throws(Exception::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir("Pictures")
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            //currentPhotoPath = absolutePath
        }
    }
}