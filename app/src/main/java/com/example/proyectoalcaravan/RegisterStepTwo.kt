package com.example.proyectoalcaravan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepTwoBinding
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.DatePickerFragment
import com.example.proyectoalcaravan.views.ImagePickerDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage


class RegisterStepTwo : Fragment() {
    private var _binding: FragmentRegisterStepTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
//    private val storage = Firebase.storage("https://console.firebase.google.com/project/login-android-e42b8/storage/login-android-e42b8.appspot.com/files")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

//    val args: registerStepTwo2Args by navArgs()
//    val updateString = args.update

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val storage = Firebase.storage("gs://login-android-e42b8.appspot.com")


//        val args: RegisterStepTwoArgs by navArgs()
//        val updateString = args.update

        //Data

//        binding.Registrar.setOnClickListener {
//            // Access the values from the ViewModel
//            val email = viewModel.email.value
//            val password = viewModel.password.value
//            val birthday = viewModel.birthday.value
//
//            val nombre = binding.etNombre.text.toString()
//            val apellido = binding.etApellido.text.toString()
//            val cedula = binding.etCedula.text.toString().toIntOrNull()
//            val telefono = binding.etTelefono.text.toString().toLongOrNull()
//
//
//
//
//            if (email != null && password != null) {
//                val user = User(email = email, password = password, firstName = nombre, lastName = apellido, cedula = cedula, phone = telefono, imageProfile = "", birthday = birthday, lat = "", listActivities = viewModel.listOfActivities.value, lgn = "", rol = viewModel.rol.value, gender = viewModel.genero.value  )
//                viewModel.createUser(user)
//            } else {
//                // Handle the case when the values are not available
//            }
//        }

        binding.Registrar.setOnClickListener {
            val email = viewModel.email.value
            val password = viewModel.password.value
            val birthday = viewModel.birthday.value
            val nombre = binding.etNombre.text.toString()
            val apellido = binding.etApellido.text.toString()
            val cedula = binding.etCedula.text.toString().toIntOrNull()
            val telefono = binding.etTelefono.text.toString().toLongOrNull()

            if (email != null && password != null) {
                var user = User(
                    email = email,
                    password = password,
                    firstName = nombre,
                    lastName = apellido,
                    cedula = cedula,
                    phone = telefono,
                    imageProfile = "", // Initialize with an empty string
                    birthday = birthday,
                    lat = "",
                    listActivities = viewModel.listOfActivities.value,
                    lgn = "",
                    rol = viewModel.rol.value,
                    gender = viewModel.genero.value
                )

                // Get the selected image URI
                val imageUri = viewModel.profileImage.value

                if (imageUri != null) {
                    // Define a reference to Firebase Storage
                    val storageRef = storage.reference.child("${user.email}.jpg")

                    // Upload the image to Firebase Storage
                    val uploadTask = storageRef.putFile(imageUri)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // The image has been successfully uploaded
                        val downloadUri = taskSnapshot.metadata?.reference?.downloadUrl
                        if (downloadUri != null) {
                            user.imageProfile = downloadUri.toString()
                            viewModel.createUser(user)
                        } else {
                            showToast("Failed to retrieve download URL.")
                        }
                    }.addOnFailureListener { exception ->
                        // Handle the error
                        if (exception is StorageException) {
                            // Handle storage-specific errors
                            Log.e("firebase eror", "Storage Error: ${exception.message}")
                            showToast("Storage Error: ${exception.message}")
                        } else {
                            // Handle other non-storage-related exceptions
                            showToast("Failed to upload image. Please try again.")
                        }
                    }

                }
            }
        }

        //Pintar imagen
        viewModel.profileImage.observe(viewLifecycleOwner){
            binding.btnImagePicker.setImageURI(viewModel.profileImage.value)
        }

        binding.fechaNacimientoTextInputLayout.setOnClickListener{
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }

        binding.btnUbicacion.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerStepTwo2_to_googleMapsFragment)
        }

        binding.btnImagePicker.setOnClickListener {
            val dialog = ImagePickerDialogFragment()
            dialog.show(childFragmentManager, "image_picker_dialog")
        }

        //Spinners para seleccionar sexo y rol
        val genderSpinner = binding.genderSpinner
        val roleSpinner = binding.roleSpinner

        // Create an ArrayAdapter using a string array and a default spinner layout
        val genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item)
        val roleAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.role_array, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        genderSpinner.adapter = genderAdapter
        roleSpinner.adapter = roleAdapter

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = parent?.getItemAtPosition(position) as String
                viewModel.genero.observe(viewLifecycleOwner){
                    viewModel.genero.value = selectedGender
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }

        viewModel.birthday.observe(viewLifecycleOwner){date->
            binding.fechaNacimientoTextInputLayout.text = date
        }

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRole = parent?.getItemAtPosition(position) as String
                viewModel.rol.observe(viewLifecycleOwner){
                    viewModel.rol.value = selectedRole
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }


        }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}