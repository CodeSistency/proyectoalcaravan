package com.example.proyectoalcaravan.views.compose.materia

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Materia(navController: NavHostController) {
    Column {
        Text(text = "Asignacion")
        Button(onClick = { navController.popBackStack()}) {
            Text(text = "Atras")
        }
    }
}