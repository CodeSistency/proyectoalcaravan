package com.example.proyectoalcaravan.views.componentes.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

//@Composable
//fun ShimmerList() {
//    // Initialize a list of items you want to display
//    val itemList = List(10) { "Item $it" }
//
//    // Use LazyColumn to create a list
//    LazyColumn(
//        modifier = Modifier.background(Color.Gray)
//    ) {
//        items(itemList.size) { index ->
//            // Apply shimmer effect to every other item
//            if (index % 2 == 0) {
//                ShimmerItem()
//            } else {
//                // Your regular item layout
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                        .background(Color.LightGray)
//                ) {
//                    // Your regular item content goes here
//                    // In this example, it's just a simple Box with a gray background
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.Gray)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ShimmerItem() {
//    // Apply shimmer effect to the entire item layout
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .background(Color.LightGray)
//            .shimmer() // Apply shimmer effect here
//    ) {
//        // Your regular item content goes here
//        // In this example, it's just a simple Box with a gray background
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.LightGray)
//        )
//    }
//}
@Composable
fun ShimmerCard() {
    // Apply shimmer effect to each card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp)

            .background(Color.LightGray)
            .shimmer()

            ,
            elevation = 8.dp// Apply shimmer effect here
    ) {
        // Your card content goes here
        // In this example, it's just a simple Box with a gray background
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.LightGray).shimmer()
//        )
    }
}

@Composable
fun ShimmerCardList() {
    // Apply shimmer effect to the entire card list layout
    val itemList = List(15) { "Item $it" }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shimmer()
            .background(Color.White)
            // Apply shimmer effect here
    ) {
        // LazyColumn to create a list of shimmer cards
        LazyColumn(
            modifier = Modifier.background(Color.White)
        ) {
            // Repeat the shimmer card for demonstration (replace with your actual data)
            items(itemList.size){
                ShimmerCard()
            }
        }
    }
}

