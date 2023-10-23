package com.example.proyectoalcaravan.model.remote


data class Materia(
    val id: Int,
    val idTeacher: Int,
    val listStudent: List<User>,
    val name: String
)