package com.example.reparacionesceti

import android.content.Intent
import android.os.Bundle
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

    private fun init() {
        etNameRegister = findViewById(R.id.etNameRegister)
        etEmailRegister = findViewById(R.id.etEmailRegister)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        etConfirmPasswordRegister = findViewById(R.id.etConfirmPasswordRegister)
        chipGroupUserRole = findViewById(R.id.chipGroupUserRole)
        btnDoRegister = findViewById(R.id.btnDoRegister)
        btLogin = findViewById(R.id.btnLoginInstead)


        btnDoRegister.setOnClickListener {
            register()
        }

        btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun register() {
        if (etNameRegister.text.toString().isNotBlank() && etEmailRegister.text.toString().isNotBlank() &&
                    etPasswordRegister.text.toString().isNotBlank() && etConfirmPasswordRegister.text.toString().isNotBlank() &&
                    etNameRegister.text.toString().isNotEmpty() && etEmailRegister.text.toString().isNotEmpty() &&
                    etPasswordRegister.text.toString().isNotEmpty() && etConfirmPasswordRegister.text.toString().isNotEmpty() &&
                    getCheckedChipText() != "") {

            val user = User(
                name = etNameRegister.text.toString(),
                email = etEmailRegister.text.toString(),
                password = etPasswordRegister.text.toString(),
                role = getCheckedChipText()
            )

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch { db.userDao().insert(user) }

            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
            finish()
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