package com.example.reparacionesceti

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())

            // Apply padding to the root view of your activity
            view.updatePadding(
                left = insets.left,
                top = insets.top, // This will push content below the status bar & notch
                right = insets.right,
                bottom = insets.bottom // This will push content above the navigation bar
            )

            // Return CONSUMED if you've handled the insets
            WindowInsetsCompat.CONSUMED
            // Or return windowInsets if you want them to be propagated further
            // windowInsets
        }

        // Vinculamos el BottomNavigationView con el Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)
    }
}