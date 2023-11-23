package com.example.proyectoalcaravan.views.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Home(
    onNavigateToProfesor: () -> Unit,
    onNavigateToStudent: () -> Unit,

    ){
    Column {
        Button(onClick = { onNavigateToProfesor()
        println("is it working")
        }) {
            Text(text = "Profesor")
        }
        Button(onClick = { onNavigateToStudent()
            println("is it working")
        }) {
            Text(text = "Estudiante")
        }
    }
}