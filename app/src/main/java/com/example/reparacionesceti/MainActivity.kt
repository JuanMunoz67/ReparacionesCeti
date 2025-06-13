package com.example.reparacionesceti

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.reparacionesceti.preferences.Preferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var userRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())

            view.updatePadding(
                left = insets.left,
                top = insets.top,
                right = insets.right,
                bottom = insets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        userRole = Preferences.currentUser?.role

        // Vinculamos el BottomNavigationView con el Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)

        adjustBottomNavigationMenu(bottomNavigationView.menu)
    }

    private fun adjustBottomNavigationMenu(menu: Menu) {
        val navReportes = menu.findItem(R.id.nav_reportes)
        val navUsuarios = menu.findItem(R.id.nav_usuarios)
        val navStats = menu.findItem(R.id.nav_stats)
        val navPerfil = menu.findItem(R.id.nav_perfil)
        val navNotificaciones = menu.findItem(R.id.nav_notificaciones)

        navReportes.isVisible = true
        navUsuarios.isVisible = userRole == "admin"
        navStats.isVisible = userRole == "admin"
        navPerfil.isVisible = true
        navNotificaciones.isVisible = true
    }
}