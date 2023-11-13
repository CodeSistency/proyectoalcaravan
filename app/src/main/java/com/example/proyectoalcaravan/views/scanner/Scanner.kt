package com.example.proyectoalcaravan.views.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.views.profesor.ProfesorFragmentDirections
import java.util.concurrent.Executors

@ExperimentalGetImage
@Composable
fun Scanner(view: View) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var qrCodeValue by remember { mutableStateOf("") }

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted

            if (!granted) {
                // User denied the permission, show a toast or handle accordingly
                Toast.makeText(context, "Permiso de la camara denegado, vaya a los ajustes", Toast.LENGTH_SHORT).show()
            }
        }
    )
    LaunchedEffect(qrCodeValue){
        Log.e("scanner", "scaneado")
    }





LaunchedEffect(key1 = true) {
    if (!hasCamPermission){
        launcher.launch(Manifest.permission.CAMERA)
    }
}

if (hasCamPermission) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(900.dp)
            .background(Color.Black)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        AndroidView({ context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also { it ->
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { qrCodeValue = it })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                    )

                } catch (exc: Exception) {
                    Log.e("DEBUG", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))
            previewView
        },
            modifier = Modifier
                .fillMaxSize())

        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.Transparent)
                .border(2.dp, Color.White)
                .drawBehind {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2.dp.toPx()
                    )
                }
        )
    }
    Text(
        text = "QR Code Value: $qrCodeValue",
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black)
            .fillMaxWidth()
        ,

        style = TextStyle(color = Color.White)
    )


    // Display the QR code value in a Text composable


    if (!qrCodeValue.isNullOrEmpty()){
        view?.findNavController()
            ?.navigate(ProfesorFragmentDirections.actionProfesorFragmentToProfileFragment(qrCodeValue.toInt() ?: 100000))
    }
}else{

}
}