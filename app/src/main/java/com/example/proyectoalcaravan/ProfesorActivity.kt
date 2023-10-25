package com.example.proyectoalcaravan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class ProfesorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Profesor()
        }
    }

    @Composable
    fun Profesor(){

        Button(onClick = { /*TODO*/ }) {
            Text(text = "Vista Profesor compose")
        }
    }
}