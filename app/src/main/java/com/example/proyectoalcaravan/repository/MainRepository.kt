package com.example.proyectoalcaravan.repository

import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.local.UserDao
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.model.remote.User
import retrofit2.Call

class MainRepository(private val retrofitService: RetrofitService, val userDao: UserDao) {

    fun getAllUsers() = retrofitService.getAllUsers()

    fun createUser(user: User): Call<User> = retrofitService.createUser(user)

    fun updateUser(userId: Int, user: User): Call<User> = retrofitService.updateUser(userId, user)

    fun deleteUser(userId: Int): Call<Void> = retrofitService.deleteUser(userId)

    fun getUserById(userId: Int): Call<User> = retrofitService.getUserById(userId)

    //Room

//    suspend fun getAllUsersDB() = userDao.getAllUsers()

    suspend fun createUserDB(user: UserDB) = userDao.insertUser(user)

//    suspend fun updateUserDB(user: User) = userDao.updateUser(user)

//    suspend fun deleteUserDB(userId: Int) = userDao.deleteUser(userId)

    suspend fun getUserByIdDB(userId: Int) = userDao.getUserById(userId)



}