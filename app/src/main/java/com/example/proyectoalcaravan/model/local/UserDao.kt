package com.example.proyectoalcaravan.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): List<UserDB>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): UserDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserDB)

//    @Update
//    fun updateUser(userId: Int)

    @Delete
    fun deleteUser(user: UserDB)
}