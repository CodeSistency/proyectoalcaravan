package com.example.proyectoalcaravan.views.componentes.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfileShimmer() {
    Column(
        modifier = Modifier.background(Color.White).fillMaxSize()

    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(20.dp)
                .background(Color.LightGray)
                .shimmer() // Apply shimmer effect here
        ) {
            // Profile image with a shimmer circle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .shimmer()
            )

            // Spacing between profile image and other elements
            Spacer(modifier = Modifier.height(16.dp))

            // Additional elements with shimmer effect
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Shimmer effect for name
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f) // Adjust width as needed
                        .height(20.dp)
                        .background(Color.Gray)
                        .shimmer() // Apply shimmer effect here
                )

                // Spacing between elements
                Spacer(modifier = Modifier.height(8.dp))

                // Shimmer effect for bio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Gray)
                        .shimmer() // Apply shimmer effect here
                )
            }
        }
    }
    // Apply shimmer effect to the entire profile layout

}