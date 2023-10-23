package com.example.proyectoalcaravan.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserDB)

    @Update
    suspend fun updateUser(user: UserDB)

    @Delete
    suspend fun deleteUser(user: Int)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserDB>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserDB?
}