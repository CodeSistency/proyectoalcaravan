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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoalcaravan.R

@Composable
fun Header(titulo: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)

            .background(
//                    Color.Blue.copy(alpha = 0.8F),
                brush = Brush.verticalGradient(
                    listOf(
//                            colorResource(id = R.color.secondary),
//                            colorResource(id = R.color.primary)
                        colorResource(id = R.color.accent2),
                        colorResource(id = R.color.blue_dark)
                    )
                ),
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
                color = Color.White


            )
        }
    }
}