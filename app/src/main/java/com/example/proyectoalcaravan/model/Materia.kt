package com.example.proyectoalcaravan.model

data class Materia(
    val id: Int,
    val idTeacher: Int,
    val listStudent: List<User>,
    val name: String
)