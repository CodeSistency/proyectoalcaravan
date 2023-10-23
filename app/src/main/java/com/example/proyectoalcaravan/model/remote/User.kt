package com.example.proyectoalcaravan.model.remote

data class User(
    val birthday: String? = null,
    val cedula: Number? = null,
    val email: String? = null,
    val firstName: String? = null,
    val gender: String? = null,
    val id: Int? = null,
    val imageProfile: String? = null,
    val lastName: String? = null,
    val lat: String? = null,
    val lgn: String? = null,
    val listActivities: List<Actividad>? = null,
    val password: String? = null,
    val phone: Number? = null,
    val rol: String? = null
)