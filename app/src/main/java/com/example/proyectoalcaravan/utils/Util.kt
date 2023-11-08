package com.example.proyectoalcaravan.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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