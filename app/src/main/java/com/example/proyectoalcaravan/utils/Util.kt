package com.example.proyectoalcaravan.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.random.Random

fun isOnline (context: Context): Boolean {
    val connectivityManager =  context.getSystemService (Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // Si la versión de Android es igual o superior a la M
    val capabilities =  connectivityManager.getNetworkCapabilities (connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport (NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport (NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport (NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}

@Composable
fun generateRandomColor(): Color {
    // Define a list of basic colors
    val basicColors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Gray
    )

    // Choose a random color from the list
    val randomColorIndex = Random.nextInt(basicColors.size)
    return basicColors[randomColorIndex]
}
