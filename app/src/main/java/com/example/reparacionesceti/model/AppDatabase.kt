package com.example.reparacionesceti.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reparacionesceti.model.dao.ReporteDao
import com.example.reparacionesceti.model.dao.UserDao
import com.example.reparacionesceti.model.entities.Reporte
import com.example.reparacionesceti.model.entities.User

@Database(entities = [Reporte::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
