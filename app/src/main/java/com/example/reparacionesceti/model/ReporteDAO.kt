package com.example.reparacionesceti.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(reporte: Reporte)

    @Query("SELECT * FROM reportes ORDER BY fecha DESC")
    fun observarTodos(): LiveData<List<Reporte>>

    @Query("SELECT * FROM reportes ORDER BY fecha DESC")
    suspend fun obtenerTodos(): List<Reporte>

    @Update
    suspend fun actualizar(reporte: Reporte)
}
