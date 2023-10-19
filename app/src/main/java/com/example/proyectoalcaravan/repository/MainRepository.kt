package com.example.proyectoalcaravan.repository

import com.example.proyectoalcaravan.model.remote.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {
    fun getAllUsers() = retrofitService.getAllUsers()
}