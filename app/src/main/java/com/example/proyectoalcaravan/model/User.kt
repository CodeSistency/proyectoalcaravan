package com.example.proyectoalcaravan.model

data class User(
    val birthday: String,
    val cedula: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val id: Int,
    val imageProfile: String,
    val lastName: String,
    val lat: String,
    val lgn: String,
    val listActivities: List<Actividad>,
    val password: String,
    val phone: String,
    val rol: String
)