package com.example.reparacionesceti.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var email: String,
    var password: String,
    var role: String
)