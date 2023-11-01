package com.example.proyectoalcaravan.views.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header2(titulo: String, materia: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
//                    Color.Blue.copy(alpha = 0.8F),
                Color.Black,
                shape = RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp),

                )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = titulo,
                style = MaterialTheme.typography.body1,
                fontSize = 50.sp,
//                    fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp)


            )
//            Text(text = nombre,
//                style = MaterialTheme.typography.body2,
//                fontSize = 20.sp,
////                    fontWeight = FontWeight.Bold,
//                color = Color.White
//
//
//            )
            Text(text = materia,
                style = MaterialTheme.typography.caption,
                fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp)


            )
        }
    }
}