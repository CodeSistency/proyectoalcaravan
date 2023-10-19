package com.example.proyectoalcaravan.model

data class Actividad(
    val calification: Int,
    val calificationRevision: Int,
    val date: Int,
    val description: String,
    val id: Int,
    val idClass: Int,
    val imageRevision: String,
    val isCompleted: Boolean,
    val messageStudent: String,
    val title: String
)