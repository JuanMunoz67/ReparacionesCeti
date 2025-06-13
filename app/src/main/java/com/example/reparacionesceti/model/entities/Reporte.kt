package com.example.reparacionesceti.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class Reporte(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var titulo: String,
    var ubicacion: String,
    var descripcion: String,
    var notas: String?,
    var estado: String,
    var imagenUri: String?,
    val fecha: String
)
