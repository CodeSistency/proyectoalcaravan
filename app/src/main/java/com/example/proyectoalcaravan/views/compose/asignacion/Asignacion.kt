package com.example.proyectoalcaravan.views.compose.asignacion

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Asignacion(navController: NavHostController) {
    Column {
        Text(text = "Asignacion")
        Button(onClick = { navController.popBackStack()}) {
            Text(text = "Atras")
        }
    }
}