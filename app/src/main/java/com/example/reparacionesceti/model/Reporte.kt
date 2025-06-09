package com.example.reparacionesceti.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class Reporte(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val ubicacion: String,
    val descripcion: String,
    val estado: String,
    val imagenUri: String?,
    val fecha: Long
)
