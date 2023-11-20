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
import com.bumptech.glide.Glide
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepTwoBinding
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.DatePickerFragment
import com.example.proyectoalcaravan.views.DialogGoogleMaps
import com.example.proyectoalcaravan.views.ImagePickerDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import java.text.DecimalFormat
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
        if (args.update && args.profile == 10000){

            binding.btnUbicacion.setOnClickListener {
                val dialogFragment = DialogGoogleMaps()
                dialogFragment.show(parentFragmentManager, dialogFragment.tag)
            }
            Log.e("checking", args.profile.toString())
//            viewModel.getUserById(args.profile, requireContext())
            binding.tvRegistroTitulo.text = "Actualizar"

            viewModel.getUserDB(1)

            viewModel.currentUserDB.observe(viewLifecycleOwner){ dataUser->
                Log.e("updated User", dataUser.toString())
                Log.e("updated User", dataUser.toString())
                var firstName = dataUser?.firstName ?: ""
                var lastName = dataUser?.lastName ?: ""
                var email = dataUser?.email ?: ""

                var cedula = dataUser?.cedula.toString() // Convert to String
                var phone = "0" + dataUser?.phone.toString() // Convert to String
                var edad = dataUser?.edad?.toString() ?: "0"
                var lat = dataUser?.lat.toString()
                var role = dataUser?.rol.toString()
                var gender = dataUser?.gender.toString()
                var birthday = dataUser?.birthday.toString()
                var password = dataUser?.gender.toString()
                var userId = dataUser?.userId

                var imageUri = viewModel.profileImage.value

                var listOfActivities = dataUser?.listActivities
                var listOfMaterias = dataUser?.listOfMaterias

                var lgn = dataUser?.lgn.toString()
                var image = dataUser?.imageProfile.toString()

                viewModel.birthday.postValue(dataUser?.birthday)


                binding.etNombre.setText(firstName)
                binding.etApellido.setText(lastName)
                binding.etCedula.isEnabled = false
                binding.etCedula.setText(cedula)
                binding.etTelefono.setText(phone)
                viewModel.rol.postValue(dataUser?.rol)
                viewModel.genero.postValue(dataUser?.gender)
//                viewModel.genero.postValue(birthday)

                binding.etEdad.setText(edad)

                binding.Registrar.text = "Actualizar"

                Glide.with(this)
                    .load(dataUser?.imageProfile)
                    .placeholder(R.drawable.ic_camera) // Placeholder image while loading
                    .error(R.drawable.ic_camera) // Error image if loading fails
                    .into(binding.btnImagePicker)


                binding.Registrar.setOnClickListener {
                    binding.Registrar.isEnabled = false

                    val userList = viewModel.userList.value
                    var validado = true

                    var modifiedFirstName = binding.etNombre.text.toString()
                    var modifiedLastName = binding.etApellido.text.toString()
                    var modifiedPhone = binding.etTelefono.text.toString()
                    var modifiedEdad = binding.etEdad.text.toString()
                    Log.e("Elements", modifiedFirstName + modifiedLastName + modifiedPhone + modifiedEdad)


                    Log.e("Elements", email.toString() + password?.toString() + modifiedFirstName.toString() + modifiedLastName.toString() + cedula.toString() + imageUri.toString() + birthday.toString() + lat.toString() + lgn.toString())




                    val lgnModified = viewModel.longitude.value
                    val latModified = viewModel.latitude.value
                    val imageUri = viewModel.profileImage.value

                    if (email.isNullOrEmpty() || password.isNullOrEmpty() || modifiedFirstName.isEmpty() || modifiedLastName.isEmpty() ||
                        cedula == null || birthday.isNullOrEmpty() || lat == null || lgn == null || image.isNullOrEmpty() ) {
                        showToast("Llena todos los campos")
                        validado = false
                        binding.Registrar.isEnabled = true

                    }




                    // Rest of your code for validation and user creation...

//                    // After all validations and user creation logic
//                    if (validado) {
//                        // Enable the button after successful validation and user creation
//                        binding.Registrar.isEnabled = true
//                    } else {
//                        // If validation fails, enable the button to allow corrections
//                        binding.Registrar.isEnabled = true
//                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (modifiedFirstName.any { it.isDigit() } || modifiedLastName.any { it.isDigit() }) {
                            showToast("El nombre y el apellido no pueden contener números")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (cedula != null) {
                            if (cedula.toInt() < 10000000 ) {
                                showToast("La cedula no es valida, debe ser mayor que diez millones")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }
                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (modifiedEdad != null) {
                            if (modifiedEdad.toInt() > 100 || modifiedEdad.toInt() < 14) {
                                showToast("La edad no es valida")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }




                    if (validado) {
                        // Check if telefono is not null and is a number
                        if (modifiedPhone != null) {
                            // Check if telefono starts with 0 and has a maximum length of 11 digits
                            if (modifiedPhone.startsWith("0") && phone.length == 11) {
                                // Phone number is valid
//                                showToast("El número es válido")
                                validado = true
                            } else {
                                // Phone number is not valid
                                showToast("El número no es válido")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        } else {
                            // telefono is null or not a number
                            showToast("Ingrese un número de teléfono válido")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers

                        if (viewModel.rol.value != null) {
                            if (viewModel.rol.value == "Seleccionar") {
                                Log.e("rol", viewModel.rol.value.toString())
                                showToast("Seleccione un rol")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        } else{
                            binding.Registrar.isEnabled = true

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
//                        val user = User(
//                            id = userId,
//                            email = email?.lowercase(),
//                            password = password,
//                            firstName = modifiedFirstName.lowercase(),
//                            lastName = modifiedLastName.lowercase(),
//                            cedula = cedula.toInt(),
//                            edad = modifiedEdad.toInt(),
//                            phone = modifiedPhone.toLongOrNull(),
//                            imageProfile = "", // Initialize with an empty string
//                            birthday = birthday,
//                            lat =  if (latModified != null) latModified else lgn?.toDouble(),
//                            listActivities = listOfActivities,
//                            listOfMaterias = listOfMaterias,
//                            lgn =  if (lgnModified != null) lgnModified else lgn?.toDouble(),
//                            rol = role,
//                            gender = gender,
//                            created = LocalDate.now(),
//                        )
                        val user = User(
                            id = userId,
                            email = email?.lowercase(),
                            password = password,
                            firstName = modifiedFirstName.lowercase(),
                            lastName = modifiedLastName.lowercase(),
                            cedula = cedula.toInt(),
                            edad = modifiedEdad.toInt(),
                            phone = modifiedPhone.toLongOrNull(),
                            imageProfile = image, // Initialize with an empty string
                            birthday = birthday,
                            lat = if (viewModel.latitude.value != null) viewModel.latitude.value else lat?.toDouble(),
                            listActivities = listOfActivities,
                            listOfMaterias = listOfMaterias,
                            lgn = if (viewModel.longitude.value != null) viewModel.longitude.value else lgn?.toDouble(),
                            rol = if (viewModel.rol.value != null) viewModel.rol.value else role,
                            gender = if (viewModel.genero.value != null) viewModel.genero.value else gender,
                            created = LocalDate.now(),
                        )

                        if(imageUri == null && image.isNotNull()){
                            if (userId != null) {
                                viewModel.updateUser(userId, user, requireContext()).observe(viewLifecycleOwner){
                                    if(it){
                                        binding.Registrar.isEnabled = true
                                    }else{
                                        binding.Registrar.isEnabled = true
                                    }
                                }
                            }
                        }else if(imageUri.isNotNull()){
                            val storageRef = storage.reference.child("${user.email}.jpg")

                            // Upload the image to Firebase Storage
                            val uploadTask = storageRef.putFile(imageUri!!)

                            uploadTask.addOnSuccessListener { taskSnapshot ->
                                // The image has been successfully uploaded
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val downloadUri = uri.toString()
                                    user.imageProfile = downloadUri
                                    if (userId != null) {
                                        viewModel.updateUser(userId, user, requireContext()).observe(viewLifecycleOwner){
                                            if(it){
                                                binding.Registrar.isEnabled = true
//                                                viewModel.createUserDB(UserDB(1, user.firstName, user.lastName, user.birthday, user.cedula, user.gender, user.imageProfile, user.email, user.password, user.rol, user.phone, user.lgn, user.lat))
                                                viewModel.getAllUsers(requireContext())
                                                viewModel.profileImage.postValue(null)
                                                viewModel.createUserDB(UserDB(1, user?.id, user?.firstName, user?.lastName, user?.birthday, edad = user?.edad, cedula = user?.cedula, user?.gender, user?.imageProfile,user?.email, user?.password, user?.rol, user?.phone, user?.lgn, user?.lat, user?.listActivities, user?.listOfMaterias))
//                                                viewModel.currentUser.postValue(user)
                                                view
                                                    ?.findNavController()
                                                    ?.navigate(R.id.action_registerStepTwo2_to_profileFragment)
                                            }else{
                                                binding.Registrar.isEnabled = true

                                            }
                                        }
                                    }
//
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
                        }else{

                        }
                        // Define a reference to Firebase Storage

                    }
                }

            }
        }else if (args.update && args.profile != 3000){

            binding.btnUbicacion.setOnClickListener {
                val dialogFragment = DialogGoogleMaps()
                dialogFragment.show(parentFragmentManager, dialogFragment.tag)
            }
            Log.e("checking", args.profile.toString())
//            viewModel.getUserById(args.profile, requireContext())
            binding.tvRegistroTitulo.text = "Actualizar"

            binding.Registrar.text = "Actualizar"


            viewModel.updatedUser.observe(viewLifecycleOwner){ dataUser->
                Log.e("updated User", dataUser.toString())
                Log.e("updated User", dataUser.toString())
                val firstName = dataUser?.firstName ?: ""
                val lastName = dataUser?.lastName ?: ""
                val email = dataUser?.email ?: ""

                val cedula = dataUser?.cedula.toString() // Convert to String
                val phone = "0" + dataUser?.phone.toString() // Convert to String
                val edad = dataUser?.edad?.toString() ?: "0"
                val lat = dataUser?.lat.toString()
                val role = dataUser?.rol.toString()
                var gender = dataUser?.gender.toString()
                val birthday = dataUser?.gender.toString()
                val password = dataUser?.gender.toString()
                val userId = dataUser?.id

                val imageUri = viewModel.profileImage.value

                val listOfActivities = dataUser?.listActivities
                val listOfMaterias = dataUser?.listOfMaterias

                val lgn = dataUser?.lgn.toString()
                val image = dataUser?.imageProfile.toString()

                viewModel.birthday.postValue(dataUser?.birthday)

                viewModel.latitude.postValue(dataUser?.lat)
                viewModel.longitude.postValue(dataUser?.lgn)





                binding.etNombre.setText(firstName)
                binding.etApellido.setText(lastName)
                 binding.etCedula.isEnabled = false
                binding.etCedula.setText(cedula)
                binding.etTelefono.setText(phone)
                viewModel.rol.postValue(role)
                viewModel.genero.postValue(gender)

                binding.etEdad.setText(edad)

                binding.Registrar.text = "Actualizar"

                Glide.with(this)
                    .load(dataUser?.imageProfile)
                    .placeholder(R.drawable.ic_camera) // Placeholder image while loading
                    .error(R.drawable.ic_camera) // Error image if loading fails
                    .into(binding.btnImagePicker)


                binding.etCedula.isEnabled = false

                    binding.roleSpinner.isEnabled = false


                binding.Registrar.setOnClickListener {
                    binding.Registrar.isEnabled = false

                    val userList = viewModel.userList.value
                    var validado = true
                    var modifiedFirstName = binding.etNombre.text.toString()
                    var modifiedLastName = binding.etApellido.text.toString()
                    var modifiedPhone = binding.etTelefono.text.toString()
                    var modifiedEdad = binding.etEdad.text.toString()
                    Log.e("Elements", modifiedFirstName + modifiedLastName + modifiedPhone + modifiedEdad)


                    Log.e("Elements", email.toString() + password?.toString() + modifiedFirstName.toString() + modifiedLastName.toString() + cedula.toString() + imageUri.toString() + birthday.toString() + lat.toString() + lgn.toString())


//                    val modifiedBirthday = viewModel.birthday.observe(viewLifecycleOwner){
//                        it
//                    }
//
//                    val rol = viewModel.rol
//                    viewModel.genero.observe(viewLifecycleOwner){
//                        gender = it
//                    }


//                    val lgn = viewModel.longitude.observe(viewLifecycleOwner){
//                        it
//                    }
//                    Log.e("longitud", lgn.toString())
//                    val lat = viewModel.latitude.observe(viewLifecycleOwner){
//                        it
//                    }
//                    Log.e("latitud", lat.toString())
                    val lgnModified = viewModel.longitude.value
                    val latModified = viewModel.latitude.value
                    val imageUri = viewModel.profileImage.value


                    Log.e("Modified Edad", modifiedEdad)


                            if (email.isNullOrEmpty() || password.isNullOrEmpty() || modifiedFirstName.isEmpty() || modifiedLastName.isEmpty() ||
                                cedula == null || birthday.isNullOrEmpty() || lat == null || lgn == null  ) {
                                showToast("Llena todos los campos")
                                Log.e("Elements", email.toString() + password?.toString() + modifiedFirstName.toString() + modifiedLastName.toString() + cedula.toString() + imageUri.toString() + birthday.toString() + lat.toString() + lgn.toString())

                                validado = false
                                binding.Registrar.isEnabled = true

                            }




                    // Rest of your code for validation and user creation...

//                    // After all validations and user creation logic
//                    if (validado) {
//                        // Enable the button after successful validation and user creation
//                        binding.Registrar.isEnabled = true
//                    } else {
//                        // If validation fails, enable the button to allow corrections
//                        binding.Registrar.isEnabled = true
//                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (firstName.any { it.isDigit() } || lastName.any { it.isDigit() }) {
                            showToast("El nombre y el apellido no pueden contener números")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (cedula != null) {
                            if (cedula.toInt() < 10000000 ) {
                                showToast("La cedula no es valida, debe ser mayor que diez millones")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }
                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (modifiedEdad != null) {
                            if (modifiedEdad.toInt() > 100 || modifiedEdad.toInt() < 14) {
                                Log.e("edad", modifiedEdad.toString())
                                showToast("La edad no es valida")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }




                    if (validado) {
                        // Check if telefono is not null and is a number
                        if (modifiedPhone != null) {
                            // Check if telefono starts with 0 and has a maximum length of 11 digits
                            if (modifiedPhone.startsWith("0") && phone.length == 11) {
                                // Phone number is valid
//                                showToast("El número es válido")
                                validado = true
                            } else {
                                // Phone number is not valid
                                showToast("El número no es válido")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        } else {
                            // telefono is null or not a number
                            showToast("Ingrese un número de teléfono válido")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    }





                    if (validado) {
                        // Check if firstName and lastName contain numbers

                        if (role != null) {
                            if (viewModel.rol.value == "Seleccionar") {
                                Log.e("rol", viewModel.rol.value.toString())
                                showToast("Seleccione un rol")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        } else{
                            binding.Registrar.isEnabled = true

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
                            firstName = modifiedFirstName.lowercase(),
                            lastName = modifiedLastName.lowercase(),
                            cedula = cedula.toInt(),
                            edad = modifiedEdad.toInt(),
                            phone = modifiedPhone.toLongOrNull(),
                            imageProfile = image, // Initialize with an empty string
                            birthday = birthday,
                            lat = if (viewModel.latitude.value != null) viewModel.latitude.value else lat?.toDouble(),
                            listActivities = listOfActivities,
                            listOfMaterias = listOfMaterias,
                            lgn = if (viewModel.longitude.value != null) viewModel.longitude.value else lgn?.toDouble(),
                            rol = if (viewModel.rol.value != null) viewModel.rol.value else role,
                            gender = if (viewModel.genero.value != null) viewModel.genero.value else gender,
                            created = LocalDate.now(),
                        )

                        if(imageUri == null && image.isNotNull()){
                            if (userId != null) {
                                viewModel.updateUser(userId, user, requireContext()).observe(viewLifecycleOwner){
                                    if(it){
                                        viewModel.profileImage.postValue(null)
                                        viewModel.latitude.postValue(null)
                                        viewModel.longitude.postValue(null)
                                        binding.Registrar.isEnabled = true
                                    }
                                }
                            }
                        }else if(imageUri.isNotNull()){
                            val storageRef = storage.reference.child("${user.email}.jpg")

                            // Upload the image to Firebase Storage
                            val uploadTask = storageRef.putFile(imageUri!!)

                            uploadTask.addOnSuccessListener { taskSnapshot ->
                                // The image has been successfully uploaded
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val downloadUri = uri.toString()
                                    user.imageProfile = downloadUri
                                    if (userId != null) {
                                        viewModel.updateUser(userId, user, requireContext()).observe(viewLifecycleOwner){
                                            if(it){
                                                binding.Registrar.isEnabled = true
                                                viewModel.profileImage.postValue(null)
                                                viewModel.latitude.postValue(null)
                                                viewModel.longitude.postValue(null)
//                                                viewModel.createUserDB(UserDB(1, user.firstName, user.lastName, user.birthday, user.cedula, user.gender, user.imageProfile, user.email, user.password, user.rol, user.phone, user.lgn, user.lat))
                                                viewModel.getAllUsers(requireContext())
                                                viewModel.profileImage.postValue(null)
                                                view
                                                    ?.findNavController()
                                                    ?.navigate(R.id.action_registerStepTwo2_to_profileFragment)
                                            }else{
                                                binding.Registrar.isEnabled = true

                                            }
                                        }
                                    }
//
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
                        }else{
                            binding.Registrar.isEnabled = true

                        }
                        // Define a reference to Firebase Storage

                    }
                }

            }
        }else{
            val email = viewModel.email.value
            val password = viewModel.password.value
            if(email == null || password == null){
                view.findNavController().navigate(R.id.registerStepOne2)
            }
            binding.Registrar.setOnClickListener {

                binding.Registrar.isEnabled = false
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

                val rol = viewModel.rol
                val genero = viewModel.genero


                val lgn = viewModel.longitude.value
                val lat = viewModel.latitude.value
                val imageUri = viewModel.profileImage.value

                if (isOnline(requireContext())){
                    if (email.isNullOrEmpty() || password.isNullOrEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                        cedula == null || birthday.isNullOrEmpty() || lat == null || lgn == null || !imageUri.isNotNull() ) {
                        showToast("Llena todos los campos")

                        Log.e("Elements", email.toString() + password?.toString() + nombre.toString() + apellido.toString() + cedula.toString() + imageUri.toString() + birthday.toString() + lat.toString() + lgn.toString())
                        validado = false
                        binding.Registrar.isEnabled = true

                    }



                    if (validado) {
                        if (nombre.any { it.isDigit() } || apellido.any { it.isDigit() }) {
                            showToast("El nombre y el apellido no pueden contener números")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    }

                    if (validado) {
                        if (cedula != null) {
                            if (cedula < 1000000 ) {
                                showToast("La cedula no es valida")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }
                    if (validado) {
                        // Check if firstName and lastName contain numbers
                        if (edad != null) {
                            if (edad > 120 || edad < 14) {
                                showToast("La edad no es valida")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        }
                    }




                if (validado) {
                    // Check if telefono is not null and is a number
                    if (telefono != null) {
                        // Check if telefono starts with 0 and has a maximum length of 11 digits
                        if (telefono.startsWith("0") && telefono.length == 11) {
                            // Phone number is valid
                            validado = true
                        } else {
                            // Phone number is not valid
                            showToast("El número no es válido")
                            validado = false
                            binding.Registrar.isEnabled = true

                        }
                    } else {
                        // telefono is null or not a number
                        showToast("Ingrese un número de teléfono válido")
                        validado = false
                        binding.Registrar.isEnabled = true

                    }
                }

                    val lat = viewModel.latitude.value
                    val lgn = viewModel.longitude.value

                    if (lat == null || lgn == null) {
                        showToast("Hay un error en la ubicacion")
                        validado = false
                        binding.Registrar.isEnabled = true
                        return@setOnClickListener
                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers

                            if (viewModel.rol.value != null) {
                                if (viewModel.rol.value == "Seleccionar") {
                                    Log.e("rol", viewModel.rol.value.toString())
                                    showToast("Seleccione un rol")
                                    validado = false
                                    binding.Registrar.isEnabled = true

                                }
                            } else{
                                binding.Registrar.isEnabled = true

                            }


                    }

                    if (validado) {
                        // Check if firstName and lastName contain numbers

                        if (viewModel.genero.value != null) {
                            if (viewModel.genero.value == "Seleccionar") {
                                Log.e("genero", viewModel.genero.value.toString())
                                showToast("Seleccione sexo")
                                validado = false
                                binding.Registrar.isEnabled = true

                            }
                        } else{
                            binding.Registrar.isEnabled = true

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
//                        }else{
//                            viewModel.showToast("Ha ocurrido un error, vuelva a intentar", requireContext())
//                            viewModel.getAllUsers(requireContext())
//                            validado = false
//                        }
//                    }

//                if (validado) {
//                    // Enable the button after successful validation and user creation
//                    binding.Registrar.isEnabled = true
//                } else {
//                    // If validation fails, enable the button to allow corrections
//                    binding.Registrar.isEnabled = true
//                }

                    if(isOnline(requireContext())){
                        if (validado) {
                            if (cedula != null) {
                                viewModel.isCedulaValid(cedula, requireContext()).observe(viewLifecycleOwner) {

                                    if(it){
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
                                            lat = viewModel.latitude.value ?: 1.00100101,
                                            listActivities = viewModel.listOfActivities.value,
                                            lgn = viewModel.longitude.value ?: 1.00100101,
                                            rol = viewModel.rol.value,
                                            gender = viewModel.genero.value,
                                            created = LocalDate.now(),
                                        )



                                        // Define a reference to Firebase Storage
                                        val storageRef = storage.reference.child("${user.email}.jpg")

                                        // Upload the image to Firebase Storage
                                        val uploadTask = imageUri?.let { it1 -> storageRef.putFile(it1) }

                                        uploadTask?.addOnSuccessListener { taskSnapshot ->
                                            // The image has been successfully uploaded
                                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                val downloadUri = uri.toString()
                                                user.imageProfile = downloadUri
                                                viewModel.createUser(user, requireContext()).observe(viewLifecycleOwner){
                                                    if(it){
                                                        viewModel.getAllUsers(requireContext())
                                                        viewModel.profileImage.postValue(null)
                                                        binding.Registrar.isEnabled = true
                                                        viewModel.latitude.postValue(null)
                                                        viewModel.longitude.postValue(null)

                                                        view
                                                            ?.findNavController()
                                                            ?.navigate(R.id.action_registerStepTwo2_to_login)
                                                    }else{
                                                        binding.Registrar.isEnabled = true


                                                    }
                                                }
                                                //                        viewModel.createUserDB(UserDB(1, user.firstName, user.lastName, user.birthday, user.cedula, user.gender, user.imageProfile, user.email, user.password, user.rol, user.phone, user.lgn, user.lat))

                                            }.addOnFailureListener { exception ->
                                                // Handle the error while trying to get the download URL
                                                showToast("Fallo para conseguir la URL: ${exception.message}")
                                            }
                                        }?.addOnFailureListener { exception ->
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
                                    }else{
                                        binding.Registrar.isEnabled = true

                                    }

                                }
                            }
                        }
                    }else{
                        binding.Registrar.isEnabled = true

                    }

                        // All fields are valid, proceed with uploading the image and creating the user

                }


            }
        }


        //Pintar imagen
        viewModel.profileImage.observe(viewLifecycleOwner){
            binding.btnImagePicker.setImageURI(viewModel.profileImage.value)
        }

        viewModel.latitude.observe(viewLifecycleOwner){ lat ->
            viewModel.longitude.observe(viewLifecycleOwner) {lag ->
                val decimalFormat = DecimalFormat("0.000")
                if(lat != null && lag != null){
                    val formattedLat = decimalFormat.format(lat)
                    val formattedLag = decimalFormat.format(lag)
                    binding.locationText.text = "lat: $formattedLat, lag: $formattedLag"
                }


            }
            Log.e("lat and lag", viewModel.latitude.toString() + viewModel.longitude.toString())

        }

        binding.fechaNacimientoTextInputLayout.setOnClickListener{
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }

//        if(args.profile != 3000){
//            if (viewModel.updatedUser.value != null){
//                viewModel.updatedUser.observe(viewLifecycleOwner){
//                    val apiKey = "AIzaSyCt7_DoZjYQM0AmrIrV00rpQDgSko_hgA8"
//
//                    // Replace 'YOUR_LATITUDE' and 'YOUR_LONGITUDE' with the actual location coordinates
//                    val latitude = it?.lat
//                    val longitude = it?.lgn
//
//                    // Set the size and scale of the static map
//                    val imageSize = "300x300"
//                    val scale = resources.displayMetrics.density.toInt()
//
//                    // Construct the Google Maps Static API URL
//                    val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap" +
//                            "?center=$latitude,$longitude" +
//                            "&zoom=15" +
//                            "&size=$imageSize" +
//                            "&scale=$scale" +
//                            "&markers=color:red%7C$latitude,$longitude" +
//                            "&key=$apiKey"
//
//                    // Load the static map image into the ImageView using Glide
//                    Glide.with(this)
//                        .load(staticMapUrl)
//                        .into(binding.miniMapImageView)
//
//                    binding.miniMapImageView.setOnClickListener {
//                        // Handle minimap click
//                        // You can add code here to navigate to the map fragment
//
//                        // Programmatically click the hidden button
//                        binding.btnUbicacion.performClick()
//                    }
//
//                    binding.btnUbicacion.setOnClickListener {
//                        // Handle the click event for the hidden button
//                        // This is where you should navigate to the map fragment
//                        view.findNavController().navigate(R.id.action_registerStepTwo2_to_googleMapsFragment)
//
//                    }
//                }
//            }
//        }else{
//            viewModel.latitude.observe(viewLifecycleOwner){ latitudeValue ->
//
//                viewModel.longitude.observe(viewLifecycleOwner){ longitudeValue ->
//                    val apiKey = "AIzaSyCt7_DoZjYQM0AmrIrV00rpQDgSko_hgA8"
//
//                    // Replace 'YOUR_LATITUDE' and 'YOUR_LONGITUDE' with the actual location coordinates
//                    val latitude = latitudeValue
//                    val longitude = longitudeValue
//
//                    // Set the size and scale of the static map
//                    val imageSize = "300x300"
//                    val scale = resources.displayMetrics.density.toInt()
//
//                    // Construct the Google Maps Static API URL
//                    val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap" +
//                            "?center=$latitude,$longitude" +
//                            "&zoom=15" +
//                            "&size=$imageSize" +
//                            "&scale=$scale" +
//                            "&markers=color:red%7C$latitude,$longitude" +
//                            "&key=$apiKey"
//
//                    // Load the static map image into the ImageView using Glide
//                    Glide.with(this)
//                        .load(staticMapUrl)
//                        .into(binding.miniMapImageView)
//
//                    binding.miniMapImageView.setOnClickListener {
//                        // Handle minimap click
//                        // You can add code here to navigate to the map fragment
//
//                        // Programmatically click the hidden button
//                        binding.btnUbicacion.performClick()
//                    }
//
//                    binding.btnUbicacion.setOnClickListener {
//                        // Handle the click event for the hidden button
//                        // This is where you should navigate to the map fragment
//                        view.findNavController().navigate(R.id.action_registerStepTwo2_to_googleMapsFragment)
//
//                    }
//                }
//                }
//
//
//        }



//        binding.btnUbicacion.setOnClickListener{
//            view.findNavController().navigate(R.id.action_registerStepTwo2_to_googleMapsFragment)
//        }
        binding.btnUbicacion.setOnClickListener {
            val dialogFragment = DialogGoogleMaps()
            dialogFragment.show(parentFragmentManager, dialogFragment.tag)
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
        viewModel.updatedUser.observe(viewLifecycleOwner){
            if (args.profile != 3000){
                genderSpinner.setSelection(genderAdapter.getPosition(
                    it?.gender
                ))

                roleSpinner.setSelection(roleAdapter.getPosition(
                    it?.rol
                ))
//                roleSpinner.setSelection(roleAdapter.getPosition("Profesor"))
            }
        }

//       else{
//
//        }






        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = parent?.getItemAtPosition(position) as String

                Log.e("selected array1", position.toString())
                viewModel.genero.postValue(selectedGender)
                viewModel.genero.observe(viewLifecycleOwner){
                    viewModel.genero.postValue(selectedGender)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }

        if(args.profile != 3000){
            viewModel.updatedUser.observe(viewLifecycleOwner){

                    binding.fechaNacimientoTextInputLayout.text = it?.birthday

            }
        }

        viewModel.birthday.observe(viewLifecycleOwner){date->
            binding.fechaNacimientoTextInputLayout.text = date
        }


        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

//                if (args.profile != 3000){
//                    var positionUpdated = 0
//                    if (viewModel.updatedUser.value?.rol == "Estudiante"){
//                        positionUpdated = 0
//                    }else{
//                        positionUpdated = 1
//                    }
//                    Log.e("selected array2", position.toString())
//
//                    val selectedRole = parent?.getItemAtPosition(positionUpdated) as String
//
//                    viewModel.rol.postValue(selectedRole)
//                    viewModel.rol.observe(viewLifecycleOwner){
//                        viewModel.rol.postValue(selectedRole)
//                    }
//                }
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

        val email = viewModel.email.value
        val password = viewModel.password.value


        viewModel.birthday.postValue(null)


        binding.etNombre.setText("")
        binding.etApellido.setText("")
        binding.etCedula.setText("")
        binding.etCedula.setText("")
        binding.etTelefono.setText("")
        viewModel.rol.postValue("")
        viewModel.genero.postValue("")

        binding.etEdad.setText("")

        viewModel.rol.postValue(null)
        viewModel.genero.postValue(null)


        viewModel.longitude.postValue(null)
        viewModel.latitude.postValue(null)
        viewModel.profileImage.postValue(null)
    }




}