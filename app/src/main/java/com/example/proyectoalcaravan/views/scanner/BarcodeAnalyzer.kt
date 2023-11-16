package com.example.proyectoalcaravan.views.scanner

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
class BarcodeAnalyzer(
    val callback: (String) -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val qrCodeValue = barcodes[0].displayValue
                        qrCodeValue?.let { it1 -> callback(it1)
                        Log.e("Qr code", it1.toString())}
                    }
                }
                .addOnFailureListener {
                    // Handle failure, if needed
                    Log.e("error scanner", it.message.toString())
                    Log.e("error scanner2", it.printStackTrace().toString())
                }
        }
        imageProxy.close()
    }
}