package com.example.reparacionesceti

import android.content.SharedPreferences

class InfoHandler (val sharedPreferences: SharedPreferences){

    companion object {
        var currentUser: User? = null
        //val users: Array<User> = arrayOf()
    }

    fun saveUser(user: User) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.
        editor.putString("id", user.id)
        editor.putString("email", user.email)
        editor.putString("pass", user.pass)
        editor.putString("name", user.name)
        editor.putString("role", user.role)
        editor.putBoolean("saved", user.saved)
        editor.apply()
    }

    fun getOrder() : Order? {
        val name = sharedPreferences.getString("name", null)
        val address = sharedPreferences.getString("address", null)
        val phone = sharedPreferences.getString("phone", null)
        val productName = sharedPreferences.getString("productName", null)
        val productCost = sharedPreferences.getString("productCost", null)
        val productSize = sharedPreferences.getString("productSize", null)

        //if (name != null && address != null && phone != null && productName != null && productCost != null && productSize != null) {

        val product = products.find { it.desc == productName }

        return Order(name.toString(), address.toString(), phone.toString(), product)
        //}

        return null
    }

    fun getUser(email: String, pass: String): User? {
        val emailS = sharedPreferences.getString("email", null)
        val passS = sharedPreferences.getString("pass", null)
        val saved = sharedPreferences.getBoolean("saved", false)
        if (emailS != null && passS != null) {
            if (emailS == email && passS == pass) {
                return User(emailS, passS, saved)
            }
        }
        return null
    }

    fun isUserSaved():Boolean {
        return sharedPreferences.getBoolean("saved", false)
    }

    fun deleteUser() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("email", "")
        editor.putString("pass", "")
        editor.putBoolean("saved", false)
        editor.apply()
    }

    fun logOut() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("saved", false)
        editor.apply()
    }

    fun clearData() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}