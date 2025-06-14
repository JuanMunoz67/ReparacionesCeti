package com.example.reparacionesceti.preferences

import android.content.SharedPreferences
import com.example.reparacionesceti.model.entities.User

class Preferences (val sharedPreferences: SharedPreferences) {

    companion object {
        val USER_SESSION = "user_session"
        var currentUser: User? = null


        //val products: Array<Product> = arrayOf(
    }

    fun saveUserSession(user: User) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("email", user.email)
        editor.putString("pass", user.password)
        editor.putString("role", user.role)
        editor.putString("name", user.name)
        editor.putInt("id", user.id)
        editor.apply()

        currentUser = user
    }

    fun getUserSession(): User? {
        val email = sharedPreferences.getString("email", null)
        val pass = sharedPreferences.getString("pass", null)
        val role = sharedPreferences.getString("role", null)
        val name = sharedPreferences.getString("name", null)
        val id = sharedPreferences.getInt("id", 0)
        if (email != null && pass != null) {
            currentUser = User(id, name!!, email, pass, role!!)
        }
        return currentUser
    }


    fun deleteUserSession() {
        currentUser = null
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("email", "")
        editor.putString("pass", "")
        editor.apply()
    }


    fun clearData() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}