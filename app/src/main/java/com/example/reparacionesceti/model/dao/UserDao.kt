package com.example.reparacionesceti.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.reparacionesceti.model.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Int): User?

    @Update
    suspend fun updateUser(user: User)

}