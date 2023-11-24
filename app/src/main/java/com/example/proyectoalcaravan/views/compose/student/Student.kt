package com.example.proyectoalcaravan.views.compose.student

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun StudentContent(navController: NavHostController) {
    Text(text = "Student")
    Button(onClick = {navController.navigate("materia")
        println("is it working")
    }) {
        Text(text = "Materia")
    }
    Button(onClick = { navController.navigate("asignacion")
        println("is it working")
    }) {
        Text(text = "Asignacion")
    }
}