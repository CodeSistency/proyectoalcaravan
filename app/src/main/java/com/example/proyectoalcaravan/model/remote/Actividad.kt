package com.example.proyectoalcaravan.model.remote

data class Actividad(
    val calification: Int = 0,
    val calificationRevision: Int = 0,
    val date: String?,
    val description: String,
    val id: Int? = null,
    val idClass: Int,
    val imageRevision: String? = null,
    val isCompleted: Boolean = false,
    val messageStudent: String? = "",
    val title: String
)