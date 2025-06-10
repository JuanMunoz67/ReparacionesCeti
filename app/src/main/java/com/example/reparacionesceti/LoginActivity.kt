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
import com.example.reparacionesceti.model.dao.UserDao
import com.example.reparacionesceti.preferences.Preferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var btRegister: Button

    private fun init() {
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        btLogin = findViewById(R.id.btnLogin)
        btRegister = findViewById(R.id.btnRegister)

        btLogin.setOnClickListener {
            lifecycleScope.launch { login() }
        }

        btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private suspend fun login() {
        if (txtEmail.text.toString().isNotBlank() && txtPassword.text.toString().isNotBlank() &&
            txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {
                val email = txtEmail.text.toString().trim()
                val password = txtPassword.text.toString()

                val db = AppDatabase.getDatabase(this)
                val user = db.userDao().getByEmailAndPassword(email, password)
                if (user != null) {
                    Preferences(getSharedPreferences(Preferences.USER_SESSION, MODE_PRIVATE)).saveUserSession(user)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }

        }
        else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}