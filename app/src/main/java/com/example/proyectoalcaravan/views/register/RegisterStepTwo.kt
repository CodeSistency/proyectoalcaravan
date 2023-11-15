package com.example.proyectoalcaravan.views.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.compose.material.darkColors
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import co.yml.charts.common.extensions.isNotNull
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepTwoBinding
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.DatePickerFragment
import com.example.proyectoalcaravan.views.ImagePickerDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import java.time.LocalDate


class RegisterStepTwo : Fragment() {
    private var _binding: FragmentRegisterStepTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
//    private val storage = Firebase.storage("https://console.firebase.google.com/project/login-android-e42b8/storage/login-android-e42b8.appspot.com/files")
    val args:RegisterStepTwoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

//    val args: registerStepTwo2Args by navArgs()
//    val updateString = args.update

    @SuppressLint("SuspiciousIndentation")
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
        if (args.update){
            Log.e("checking", args.profile.toString())
            viewModel.getUserById(args.profile, requireContext())
            binding.tvRegistroTitulo.text = "Actualizar"

            viewModel.updatedUser.observe(viewLifecycleOwner){ dataUser->
                Log.e("updated User", dataUser.toString())
                val firstName = dataUser.firstName ?: ""
                val lastName = dataUser.lastName ?: ""
                val email = dataUser.email ?: ""

                val cedula = dataUser.cedula.toString() // Convert to String
                val phone = "0"+dataUser.phone.toString() // Convert to String
                val edad = dataUser.edad?.let {
                    it.toString()
                } ?: "0"
                val lat = dataUser.lat.toString()
                val role = dataUser.rol.toString()
                val gender = dataUser.gender.toString()
                val birthday = dataUser.gender.toString()
                val password = dataUser.gender.toString()
                val userId = dataUser.id

                val imageUri = viewModel.profileImage.value

                val listOfActivities = dataUser.listActivities
                val listOfMaterias = dataUser.listOfMaterias




                val lgn = dataUser.lgn.toString()
                val image = dataUser.imageProfile.toString()

                binding.etNombre.setText(firstName)
                binding.etApellido.setText(lastName)
                binding.etCedula.isEnabled = false
                binding.etCedula.setText(cedula)
                binding.etTelefono.setText(phone)
                viewModel.rol.postValue(role)
                viewModel.genero.postValue(gender)
                viewModel.genero.postValue(birthday)


                binding.etEdad.setText(edad)

                binding.Registrar.text = "Actualizar"


                binding.Registrar.setOnClickListener {
                    val userList = viewModel.userList.value
                    var validado = true



                    if (lat != null) {
                        if (lgn != null) {
                            if (email.isNullOrEmpty() || password.isNullOrEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                                cedula == null || birthday.isNullOrEmpty() || lat == null || lgn == null || !imageUri.isNotNull() ) {
                                showToast("Llena todos los campos")
                                validado = false
                            }
                        }
                    }

                    binding.Registrar.isEnabled = false

                    // Rest of your code for validation and user creation...

                    // After all validations and user creation logic
                    if (validado) {
                        // Enable the button after successful validation and user creation
                        binding.Registrar.isEnabled = true
                    } else {
                        // If validation fails, enable the button to allow corrections
                        binding.Registrar.isEnabled = true
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (firstName.any { it.isDigit() } || lastName.any { it.isDigit() }) {
                            showToast("El nombre y el apellido no pueden contener números")
                            validado = false
                        }
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (cedula != null) {
                            if (cedula.toInt() < 1000000 ) {
                                showToast("La cedula no es valida")
                                validado = false
                            }
                        }
                    }
                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (edad != null) {
                            if (edad.toInt() > 120 || edad.toInt() < 14) {
                                showToast("La edad no es valida")
                                validado = false
                            }
                        }
                    }




                    if (validado) {
                        // Check if telefono is not null and is a number
                        if (phone != null && phone.matches(Regex("\\d+"))) {
                            // Check if telefono starts with 0 and has a maximum length of 11 digits
                            if (phone.startsWith("0") && phone.length == 11) {
                                // Phone number is valid
                                showToast("El número es válido")
                            } else {
                                // Phone number is not valid
                                showToast("El número no es válido")
                                validado = false
                            }
                        } else {
                            // telefono is null or not a number
                            showToast("Ingrese un número de teléfono válido")
                            validado = false
                        }
                    }

//                    if (validado) {
//                        if (userList != null) {
//                            for (user in userList) {
//                                if (user.cedula == cedula) {
//                                    showToast("Esta cedula ya existe")
//                                    validado = false
//                                    break
//                                }
//                            }
//                        }
//                    }

                    if (validado) {
                        // All fields are valid, proceed with uploading the image and creating the user
                        val user = User(
                            id = userId,
                            email = email?.lowercase(),
                            password = password,
                            firstName = firstName.lowercase(),
                            lastName = lastName.lowercase(),
                            cedula = cedula.toInt(),
                            edad = edad.toInt(),
                            phone = phone.toLongOrNull(),
                            imageProfile = "", // Initialize with an empty string
                            birthday = birthday,
                            lat = lat.toDouble(),
                            listActivities = listOfActivities,
                            listOfMaterias = listOfMaterias,
                            lgn = lgn.toDouble(),
                            rol = role,
                            gender = gender,
                            created = LocalDate.now(),
                        )

                        // Define a reference to Firebase Storage
                        val storageRef = storage.reference.child("${user.email}.jpg")

                        // Upload the image to Firebase Storage
                        val uploadTask = storageRef.putFile(imageUri!!)

                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            // The image has been successfully uploaded
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUri = uri.toString()
                                user.imageProfile = downloadUri
                                if (userId != null) {
                                    viewModel.updateUser(userId, user, requireContext())
                                }
//                        viewModel.createUserDB(UserDB(1, user.firstName, user.lastName, user.birthday, user.cedula, user.gender, user.imageProfile, user.email, user.password, user.rol, user.phone, user.lgn, user.lat))
                                viewModel.getAllUsers(requireContext())
                                viewModel.profileImage.postValue(null)
                                view
                                    ?.findNavController()
                                    ?.navigate(R.id.action_registerStepTwo2_to_profileFragment)
                            }.addOnFailureListener { exception ->
                                // Handle the error while trying to get the download URL
                                showToast("Fallo para conseguir la URL: ${exception.message}")
                            }
                        }.addOnFailureListener { exception ->
                            // Handle the error during image upload
                            if (exception is StorageException) {
                                // Handle storage-specific errors
                                Log.e("firebase error", "Storage Error: ${exception.message}")
                                showToast("Storage Error: ${exception.message}")
                            } else {
                                // Handle other non-storage-related exceptions
                                showToast("Subida de la imagen ha fallado. Intente de nuevo")
                            }
                        }
                    }
                }

            }
        }else{

            binding.Registrar.setOnClickListener {
                val userList = viewModel.userList.value
                var validado = true

                val email = viewModel.email.value
                val password = viewModel.password.value
                val birthday = viewModel.birthday.value
                val nombre = binding.etNombre.text.toString()
                val apellido = binding.etApellido.text.toString()
                val cedula = binding.etCedula.text.toString().toIntOrNull()
                val telefono = binding.etTelefono.text.toString()
                val edad = binding.etEdad.text.toString().toIntOrNull()

                val lgn = viewModel.longitude.value
                val lat = viewModel.latitude.value
                val imageUri = viewModel.profileImage.value

                if (lat != null) {
                    if (lgn != null) {
                        if (email.isNullOrEmpty() || password.isNullOrEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                            cedula == null || birthday.isNullOrEmpty() || lat == null || lgn == null || !imageUri.isNotNull() ) {
                            showToast("Llena todos los campos")
                            validado = false
                        }
                    }
                }

                binding.Registrar.isEnabled = false

                // Rest of your code for validation and user creation...

                // After all validations and user creation logic
                if (validado) {
                    // Enable the button after successful validation and user creation
                    binding.Registrar.isEnabled = true
                } else {
                    // If validation fails, enable the button to allow corrections
                    binding.Registrar.isEnabled = true
                }

                if (validado) {
                    // Check if firstName and lastName contain numbers
                    if (nombre.any { it.isDigit() } || apellido.any { it.isDigit() }) {
                        showToast("El nombre y el apellido no pueden contener números")
                        validado = false
                    }
                }

                if (validado) {
                    // Check if firstName and lastName contain numbers
                    if (cedula != null) {
                        if (cedula < 1000000 ) {
                            showToast("La cedula no es valida")
                            validado = false
                        }
                    }
                }
                if (validado) {
                    // Check if firstName and lastName contain numbers
                    if (edad != null) {
                        if (edad > 120 || edad < 14) {
                            showToast("La edad no es valida")
                            validado = false
                        }
                    }
                }




                if (validado) {
                    // Check if telefono is not null and is a number
                    if (telefono != null && telefono.matches(Regex("\\d+"))) {
                        // Check if telefono starts with 0 and has a maximum length of 11 digits
                        if (telefono.startsWith("0") && telefono.length == 11) {
                            // Phone number is valid
                            showToast("El número es válido")
                        } else {
                            // Phone number is not valid
                            showToast("El número no es válido")
                            validado = false
                        }
                    } else {
                        // telefono is null or not a number
                        showToast("Ingrese un número de teléfono válido")
                        validado = false
                    }
                }

                if (validado) {
                    if (userList != null) {
                        for (user in userList) {
                            if (user.cedula == cedula) {
                                showToast("Esta cedula ya existe")
                                validado = false
                                break
                            }
                        }
                    }
                }

                if (validado) {
                    // All fields are valid, proceed with uploading the image and creating the user
                    val user = User(
                        email = email?.lowercase(),
                        password = password,
                        firstName = nombre.lowercase(),
                        lastName = apellido.lowercase(),
                        cedula = cedula,
                        edad = edad,
                        phone = telefono.toLongOrNull(),
                        imageProfile = "", // Initialize with an empty string
                        birthday = birthday,
                        lat = viewModel.latitude.value,
                        listActivities = viewModel.listOfActivities.value,
                        lgn = viewModel.longitude.value,
                        rol = viewModel.rol.value,
                        gender = viewModel.genero.value,
                        created = LocalDate.now(),
                    )

                    // Define a reference to Firebase Storage
                    val storageRef = storage.reference.child("${user.email}.jpg")

                    // Upload the image to Firebase Storage
                    val uploadTask = storageRef.putFile(imageUri!!)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // The image has been successfully uploaded
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUri = uri.toString()
                            user.imageProfile = downloadUri
                            viewModel.createUser(user, requireContext())
//                        viewModel.createUserDB(UserDB(1, user.firstName, user.lastName, user.birthday, user.cedula, user.gender, user.imageProfile, user.email, user.password, user.rol, user.phone, user.lgn, user.lat))
                            viewModel.getAllUsers(requireContext())
                            viewModel.profileImage.postValue(null)
                            view
                                ?.findNavController()
                                ?.navigate(R.id.action_registerStepTwo2_to_login)
                        }.addOnFailureListener { exception ->
                            // Handle the error while trying to get the download URL
                            showToast("Fallo para conseguir la URL: ${exception.message}")
                        }
                    }.addOnFailureListener { exception ->
                        // Handle the error during image upload
                        if (exception is StorageException) {
                            // Handle storage-specific errors
                            Log.e("firebase error", "Storage Error: ${exception.message}")
                            showToast("Storage Error: ${exception.message}")
                        } else {
                            // Handle other non-storage-related exceptions
                            showToast("Subida de la imagen ha fallado. Intente de nuevo")
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
        val genderAdapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.gender_array, android.R.layout.simple_spinner_item)
        val roleAdapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.role_array, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        genderSpinner.adapter = genderAdapter
        roleSpinner.adapter = roleAdapter

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = parent?.getItemAtPosition(position) as String
                viewModel.genero.postValue(selectedGender)
                viewModel.genero.observe(viewLifecycleOwner){
                    viewModel.genero.postValue(selectedGender)
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
                viewModel.rol.postValue(selectedRole)
                viewModel.rol.observe(viewLifecycleOwner){
                    viewModel.rol.postValue(selectedRole)
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