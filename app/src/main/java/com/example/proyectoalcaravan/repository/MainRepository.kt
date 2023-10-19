package com.example.proyectoalcaravan.repository

import com.example.proyectoalcaravan.model.User
import com.example.proyectoalcaravan.model.remote.RetrofitService
import retrofit2.Call

class MainRepository constructor(private val retrofitService: RetrofitService) {
    fun getAllUsers() = retrofitService.getAllUsers()

    fun createUser(user: User): Call<User> = retrofitService.createUser(user)

    fun updateUser(userId: Int, user: User): Call<User> = retrofitService.updateUser(userId, user)

    fun deleteUser(userId: Int): Call<Void> = retrofitService.deleteUser(userId)

    fun getUserById(userId: Int): Call<User> = retrofitService.getUserById(userId)


}