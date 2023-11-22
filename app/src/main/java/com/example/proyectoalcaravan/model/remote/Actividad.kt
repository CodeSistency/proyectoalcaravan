package com.example.proyectoalcaravan.model.remote

data class Actividad(
    var calification: Int = 0,
    val calificationRevision: Int = 0,
    val date: String? = "",
    val description: String = "",
    val id: Int? = null,
    val idClass: Int = 0,
    var imageRevision: String? = null,
    val isCompleted: Boolean = false,
    var messageStudent: String? = "",
    val title: String = ""
)