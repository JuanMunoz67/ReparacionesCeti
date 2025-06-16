package com.example.reparacionesceti

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reparacionesceti.model.AppDatabase
import com.example.reparacionesceti.model.entities.User
import com.example.reparacionesceti.preferences.Preferences
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var etNameRegister: EditText
    private lateinit var etEmailRegister: EditText
    private lateinit var etPasswordRegister: EditText
    private lateinit var etConfirmPasswordRegister: EditText
    private lateinit var chipGroupUserRole: ChipGroup
    private lateinit var btnDoRegister: Button
    private lateinit var btLogin: Button
    private lateinit var btnDelete: Button

    private var editMode = false
    private lateinit var db: AppDatabase
    private var user: User? = null
    private var userRole = ""
    private var userId = -1

    private fun init() {
        etNameRegister = findViewById(R.id.etTituloReporte)
        etEmailRegister = findViewById(R.id.etEmailRegister)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        etConfirmPasswordRegister = findViewById(R.id.etConfirmPasswordRegister)
        chipGroupUserRole = findViewById(R.id.chipGroupUserRole)
        btnDoRegister = findViewById(R.id.btnDoRegister)
        btLogin = findViewById(R.id.btnLoginInstead)
        btnDelete = findViewById(R.id.btnDeleteUser)


        btnDoRegister.setOnClickListener {
            register()
            finish()
        }

        btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        db = AppDatabase.getDatabase(this)

        if (Preferences.currentUser != null) {
            userRole = Preferences.currentUser!!.role
        }

        init()
        setValuesIfAny()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun setValuesIfAny() {
        userId = intent.getIntExtra("userId", -1)

        if (userId != -1 && userRole != "") {
            editMode = true
            btLogin.isEnabled = false
            btLogin.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE

            lifecycleScope.launch {
                user = db.userDao().getById(userId)

                user?.let {
                    etNameRegister.setText(it.name)
                    etEmailRegister.setText(it.email)
                    etPasswordRegister.setText(it.password)
                    etConfirmPasswordRegister.setText(it.password)
                    chipGroupUserRole.check(
                        when(it.role) {
                            "admin" -> R.id.chipAdmin
                            "estudiante" -> R.id.chipEstudiante
                            "tecnico" -> R.id.chipTecnico
                            else -> -1
                        }
                    )
                }
            }
            btnDoRegister.text = "Actualizar"

            if (userRole == "estudiante") {
                etNameRegister.isEnabled = false
                etEmailRegister.isEnabled = true
                etPasswordRegister.isEnabled = true
                etConfirmPasswordRegister.isEnabled = true
                chipGroupUserRole.isEnabled = false
                btnDelete.isEnabled = false
            }

            if (userRole == "tecnico") {
                etNameRegister.isEnabled = false
                etEmailRegister.isEnabled = true
                etPasswordRegister.isEnabled = true
                etConfirmPasswordRegister.isEnabled = true
                chipGroupUserRole.isEnabled = false
                btnDelete.isEnabled = false
            }

            if (userRole == "admin") {
                etNameRegister.isEnabled = true
                etEmailRegister.isEnabled = true
                etPasswordRegister.isEnabled = true
                etConfirmPasswordRegister.isEnabled = true
                chipGroupUserRole.isEnabled = true
                btnDelete.isEnabled = true
            }
        }


    }

    private fun register() {
        if (etNameRegister.text.toString().isNotBlank() && etEmailRegister.text.toString().isNotBlank() &&
                    etPasswordRegister.text.toString().isNotBlank() && etConfirmPasswordRegister.text.toString().isNotBlank() &&
                    etNameRegister.text.toString().isNotEmpty() && etEmailRegister.text.toString().isNotEmpty() &&
                    etPasswordRegister.text.toString().isNotEmpty() && etConfirmPasswordRegister.text.toString().isNotEmpty() &&
                    getCheckedChipText() != "") {

            val name = etNameRegister.text.toString()
            val email = etEmailRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val role = getCheckedChipText()


            lifecycleScope.launch {
                if (!editMode) {
                    user = User(
                        name = name,
                        email = email,
                        password = password,
                        role = role
                    )
                    db.userDao().insert(user!!)
                }
                else {
                    //user = db.userDao().getById(userId)
                    user?.name = name
                    user?.email = email
                    user?.password = password
                    user?.role = role
                    db.userDao().updateUser(user!!)
                    Preferences(getSharedPreferences(Preferences.USER_SESSION, 0)).saveUserSession(user!!)

                }
            }

            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, LoginActivity::class.java)

            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            //startActivity(intent)

        }
        else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCheckedChipText(): String {
        val checkedId = chipGroupUserRole.checkedChipId
        var role = ""

        if (checkedId != -1) {
            val chip = findViewById<Chip>(checkedId).text.toString()
            role =
                when (chip) {
                    "Admin" -> "admin"
                    "Estudiante" -> "estudiante"
                    "Técnico" -> "tecnico"
                    else -> ""
                }
        }
        return role
    }
}