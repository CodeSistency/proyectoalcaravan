package com.example.proyectoalcaravan.views.compose.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun CurrentProfile(navController: NavHostController) {
    Column {
        Text(text = "Profile")
        Button(onClick = { navController.popBackStack()}) {
            Text(text = "Atras")
        }
    }
}