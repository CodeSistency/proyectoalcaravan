package com.example.proyectoalcaravan.model.remote


data class Materia(
    val id: Int,
    val idTeacher: Int,
    var listStudent: List<User>? = listOf(),
    val name: String
)