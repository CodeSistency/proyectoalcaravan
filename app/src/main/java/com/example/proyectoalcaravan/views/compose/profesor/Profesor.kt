package com.example.proyectoalcaravan.views.compose.profesor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ProfesorContent(navController: NavHostController) {
    Column {
        Text(text = "Profesor")
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
}