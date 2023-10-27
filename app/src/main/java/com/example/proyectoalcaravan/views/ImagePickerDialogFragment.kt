package com.example.proyectoalcaravan.views

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log

import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentImagePickerDialogBinding
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.RESULT_ERROR

class ImagePickerDialogFragment : DialogFragment() {

    var imagePicker: ImageView? = null
    var base64ImageData: String? = null
    private val viewModel by activityViewModels<MainViewModel>()
//    private var binding: FragmentImagePickerDialogBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_image_picker_dialog, null)

//        imagePicker = view?.findViewById(R.id.profileImage)


        val btnGallery = dialogView.findViewById<ImageButton>(R.id.btnGallery)
        val btnCamera = dialogView.findViewById<ImageButton>(R.id.btnCamera)

        btnGallery.setOnClickListener {
            // Handle gallery option
//            dismiss()
            ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop()
                .maxResultSize(400, 400).start()
        }

        btnCamera.setOnClickListener {
            // Handle camera option
//            dismiss()
            ImagePicker.with(this).cameraOnly().crop().maxResultSize(400, 400).start()

        }

        builder.setView(dialogView)
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            val selectedImageUri = data?.data
            Log.e("selected image", selectedImageUri.toString())
            if (selectedImageUri != null) {
                // Convert the selected image to base64
                val base64Image = imageUriToBase64(selectedImageUri)
                viewModel.profileImage.value = base64Image
                Log.e("base64", viewModel.profileImage.value!!)
                // Display the image or do whatever you need with the base64 data
//                imagePicker?.setImageURI(selectedImageUri)
                imagePicker?.setImageBitmap(decodePicString(base64Image))
            }
        } else if (resultCode == RESULT_ERROR) {
            val error = data?.extras
            Log.e("Error image", error.toString())
            // Handle error
        }

//
    }

    private fun imageUriToBase64(imageUri: Uri): String {
        Log.e("URI", imageUri.toString())
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)

    }

    private fun decodePicString (encodedString: String): Bitmap {

        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        Log.e("bitmap", decodedImage.toString())

        return decodedImage
    }


}