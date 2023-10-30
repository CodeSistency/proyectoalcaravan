package com.example.proyectoalcaravan.model.remote

data class User(
    val birthday: String? = "",
    val cedula: Int? = null,
    val email: String? = "",
    val firstName: String? = "",
    val gender: String? = "",
    val id: Int? = null,
    var imageProfile: String? = "",
    val lastName: String? = "",
    val lat: String? = "",
    val lgn: String? = null,
    val listActivities: List<Actividad?>? = listOf(),
    val password: String? = "",
    val phone: Long? = null,
    val rol: String? = "",


    )