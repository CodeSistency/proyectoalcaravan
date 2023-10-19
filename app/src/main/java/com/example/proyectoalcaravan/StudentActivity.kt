package com.example.proyectoalcaravan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

class StudentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_student)

//        val studentView = findViewById<ComposeView>(R.id.compose_student)

//        studentView.setContent {
//            student()
//        }

        setContent{
            student()
        }
    }

    @Composable
    fun student(){
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Vista estudiante compose")
        }
    }
}