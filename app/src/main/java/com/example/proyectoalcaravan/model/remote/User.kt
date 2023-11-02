package com.example.proyectoalcaravan.model.remote

import java.time.LocalDate

data class User(
    val birthday: String? = "",
    val cedula: Int? = null,
    val email: String? = "",
    val firstName: String? = "",
    val gender: String? = "masculino",
    val id: Int? = null,
    var imageProfile: String? = "",
    val lastName: String? = "",
    val lat: Double? =0.0,
    val lgn: Double?=0.0,
    val listActivities: List<Actividad?>? = listOf(),
    val password: String? = "",
    val phone: Long? = null,
    val rol: String? = "Estudiante",
    var listOfMaterias: List<Materia>? = listOf(),
    val created: LocalDate? = LocalDate.now(),


    )