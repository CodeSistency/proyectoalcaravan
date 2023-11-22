package com.example.proyectoalcaravan.viewmodels

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.views.register.RegisterStepOne
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.repository.MainRepository
import com.example.proyectoalcaravan.utils.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository): ViewModel() {

    //Estados actuales
    var currentUser = MutableLiveData<User>()
    var currentMateria = MutableLiveData<Materia>()
    var currentActividad = MutableLiveData<Actividad>()
    var updatedUser = MutableLiveData<User?>()
    var currentUserDB = MutableLiveData<UserDB?>()
    var refreshing = MutableLiveData<Boolean>(false)
    var refreshingUserById = MutableLiveData<Boolean>(false)
    var refreshingMaterias = MutableLiveData<Boolean>(false)
    var refreshingMateriasById = MutableLiveData<Boolean>(false)
    var refreshingActividad = MutableLiveData<Boolean>(false)
    var refreshingActividadById = MutableLiveData<Boolean>(false)
    var refreshingCurrentUser = MutableLiveData<Boolean>(false)
    var refreshingUpdatedUser = MutableLiveData<Boolean>(false)
    var conexion = MutableLiveData<Boolean>(true)
    var registerStepOne = MutableLiveData<Boolean>(false)
    var modalAsignacion1 = MutableLiveData<Boolean>(false)
    var modalAsignacion2 = MutableLiveData<Boolean>(false)
    var currentNota = MutableLiveData<Double>()


    //Estados nuevos
//    private val _uiState = MutableStateFlow(User())
//    val uiState: StateFlow<User>





    //Listas de datos
    var userList = MutableLiveData<List<User>>()
    var userStudentsList = MutableLiveData<List<User>>()
    var materiasList = MutableLiveData<List<Materia>>()
    var materiasUserList = MutableLiveData<List<Materia>>()
    var activitiesList = MutableLiveData<List<Actividad>>()
    var listOfActivitiesFiltered = MutableLiveData<List<Actividad>?>()

    var activitiesListById = MutableLiveData<List<Actividad>>()
    var filteredUserList = MutableLiveData<List<User>>()

    val errorMessage = MutableLiveData<String>()

    //Variables filtradas
    var alphabetOrder = userStudentsList.value?.sortedBy { it.firstName }
    var edadOrder = userStudentsList.value?.sortedBy { it.edad }
    var birthdayOrder = userStudentsList.value?.sortedBy { it.birthday }
    var createdOrder = userStudentsList.value?.sortedBy { it.created }

    //Data to Register

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val birthday = MutableLiveData<String>("Fecha")
    val rol = MutableLiveData<String>()
    val genero = MutableLiveData<String>()
    val listOfActivities = MutableLiveData<List<Actividad?>>(listOf())
    var loggedIn = MutableLiveData<Boolean>(false)

    val profileImage = MutableLiveData<Uri?>()


    // Estados actuales
    var currentUserCompose by mutableStateOf<User?>(null)
    var currentMateriaCompose by mutableStateOf<Materia?>(null)
    var currentActividadCompose by mutableStateOf<Actividad?>(null)
    var updatedUserCompose by mutableStateOf<User?>(null)
    var currentUserDBCompose by mutableStateOf<UserDB?>(null)



    private val user_uiState = MutableStateFlow(
        User(
            birthday = "",
            cedula = null,
            edad = null,
            email = "",
            firstName = "",
            gender = "masculino",
            id = null,
            imageProfile = "",
            lastName = "",
            lat = 0.0,
            lgn = 0.0,
            listActivities = listOf(),
            password = "",
            phone = null,
            rol = "Estudiante",
            listOfMaterias = listOf(),
            created = ""
        )
    )
    val UserState: StateFlow<User> = user_uiState.asStateFlow()

    private val _listStudentUiState = MutableStateFlow<List<User>?>(emptyList())
    val listStudentUiState: StateFlow<List<User>?> = _listStudentUiState.asStateFlow()

    private val _actividadUiState = MutableStateFlow(Actividad())
    val actividadUiState: StateFlow<Actividad> = _actividadUiState.asStateFlow()

    private val _listActividadUiState = MutableStateFlow(Actividad())
    val listActividadUiState: StateFlow<Actividad> = _listActividadUiState.asStateFlow()

    var userListCompose by mutableStateOf<List<User>?>(null)
    var userStudentsListCompose by mutableStateOf<List<User>?>(null)
    var materiasListCompose by mutableStateOf<List<Materia>?>(null)
    var materiasUserListCompose by mutableStateOf<List<Materia>?>(null)
    var activitiesListCompose by mutableStateOf<List<Actividad>?>(null)
    var activitiesListByIdCompose by mutableStateOf<List<Actividad>?>(null)
    var filteredUserListCompose by mutableStateOf<List<User>?>(null)

    var currentNotaCompose by mutableStateOf<Double?>(null)

    val errorMessageCompose by mutableStateOf<String?>(null)

    var alphabetOrderCompose by mutableStateOf<List<User>?>(null)
    var edadOrderCompose by mutableStateOf<List<User>?>(null)
    var birthdayOrderCompose by mutableStateOf<List<User>?>(null)
    var createdOrderCompose by mutableStateOf<List<User>?>(null)

    val emailCompose by mutableStateOf<String?>(null)
    val passwordCompose by mutableStateOf<String?>(null)
    val latitudeCompose by mutableStateOf<Double?>(null)
    val longitudeCompose by mutableStateOf<Double?>(null)
    val birthdayCompose by mutableStateOf<String?>("Fecha")
    val rolCompose by mutableStateOf<String?>(null)
    val generoCompose by mutableStateOf<String?>(null)
    val listOfActivitiesCompose by mutableStateOf<List<Actividad?>>(listOf())
    var listOfActivitiesFilteredCompose by mutableStateOf<List<Actividad>?>(listOf())


    val profileImageCompose by mutableStateOf<Uri?>(null)


//    init {
//        // Initialize or set your user data to userList
//        // For example:
//        // userList.value = listOf(user1, user2, user3, ...)
//        // Initially, set filteredUserList to the same data
//        filteredUserList.value = userStudentsList.value
//    }

    fun prueba(){
        viewModelScope.launch {

            while (true){
                delay(1000)
                Log.e("prueba", "hola")
            }

        }
    }


    fun calculateOverallCalification(listOfActivities: List<Actividad>): Double {
        if (listOfActivities.isEmpty()) {
            // Return some default value or handle empty list case
            return 0.0
        }

        val totalCalificationRevision = listOfActivities.sumBy { it.calificationRevision }
        val totalPossibleCalification = listOfActivities.size * 100
        currentNota.postValue((totalCalificationRevision.toDouble() / totalPossibleCalification) * 100)
        currentNotaCompose = (totalCalificationRevision.toDouble() / totalPossibleCalification) * 100
        Log.e("nota", currentNota.value.toString())
        return (totalCalificationRevision.toDouble() / totalPossibleCalification) * 100
    }

    fun listWithActivities() {
        listOfActivitiesFilteredCompose = activitiesListById.value?.map { existingActivity ->
            updatedUser.value?.listActivities?.find { it?.id == existingActivity.id } ?: existingActivity
        } ?: emptyList()

        Log.e("compose activities", listOfActivitiesFilteredCompose.toString())

        listOfActivitiesFiltered.postValue(activitiesListById.value?.map { existingActivity ->
            updatedUser.value?.listActivities?.find { it?.id == existingActivity.id } ?: existingActivity
        } ?: emptyList())

        Log.e("other activities", listOfActivitiesFiltered.toString())

    }

    val updatedList = activitiesListById.value?.map { existingActivity ->
        updatedUser.value?.listActivities?.find { it?.id == existingActivity?.id } ?: existingActivity
    } ?: emptyList()

    //Busqueda
    fun setSearchQuery(query: String) {
        filteredUserList.value = if (query.isEmpty()) {
            userStudentsList.value // Show all users when the query is empty
        } else {
            userList.value?.filter { user ->
                user.firstName?.contains(query, ignoreCase = true) == true
            }
        }
        Log.e("filter by search", filteredUserList.toString())
    }

    //Filtros
    fun setAgeFilter(minAge: Int, maxAge: Int) {
        filteredUserList.value = userList.value?.filter { user ->
            user.edad in minAge..maxAge
        }
    }

    fun setEmailFilter(email: String) {
        filteredUserList.value = filteredUserList.value?.filter { user ->
            user.email?.contains(email, ignoreCase = true) == true
        }
    }

    fun setGenderFilter(gender: String) {
        filteredUserList.value = filteredUserList.value?.filter { user ->
            user.gender?.equals(gender, ignoreCase = true) == true
        }
    }


    //Order by
    fun sortByAge(ascending: Boolean = true) {
        filteredUserList.value = filteredUserList.value?.sortedBy { user ->
            user.edad ?: 0
        }
        if (!ascending) {
            filteredUserList.value = filteredUserList.value?.reversed()
        }
    }

    fun sortByAlphabetical(ascending: Boolean = true) {
        filteredUserList.value = filteredUserList.value?.sortedBy { user ->
            user.firstName ?: ""
        }
        if (!ascending) {
            filteredUserList.value = filteredUserList.value?.reversed()
        }
    }

    fun sortByBirthday(ascending: Boolean = true) {
        filteredUserList.value = filteredUserList.value?.let {
            if (ascending) {
                it.sortedBy { user ->
                    user.birthday ?: ""
                }
            } else {
                it.sortedByDescending { user ->
                    user.birthday ?: ""
                }
            }
        }
    }

    fun sortByCreationDate(ascending: Boolean = false) {
        filteredUserList.value = filteredUserList.value?.let { userList ->
            if (ascending) {
                userList.sortedBy { user ->
                    user.created ?: ""
                }
            } else {
                userList.sortedByDescending { user ->
                    user.created ?: ""
                }
            }
        }
        Log.e("sortByCreation", filteredUserList.value.toString())
    }

//    fun sortByCreationDate(ascending: Boolean = false) {
//        filteredUserList.value = filteredUserList.value?.let { userList ->
//            if (ascending) {
//                userList.sortedBy { user ->
//                    user.created ?: ""
//                }
//            } else {
//                userList.sortedByDescending { user ->
//                    user.created ?: ""
//                }
//            }
//        }
//    }

//    fun sortByCreationDate(ascending: Boolean = false) {
//        filteredUserList.value = filteredUserList.value?.let { userList ->
//            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//            if (ascending) {
//                userList.sortedBy { user ->
//                    LocalDate.parse(user.created, formatter).toEpochDay()
//                }
//            } else {
//                userList.sortedByDescending { user ->
//                    LocalDate.parse(user.created, formatter).toEpochDay()
//                }
//            }
//        }
//    }

    //Reset Filters
    fun resetFilters(context: Context) {
        // Reset all filters and show all users
        getUserStudents("Estudiante", context)
        filteredUserList.value = userStudentsList.value?.toMutableList()    }

    fun insertContact(context: ComponentActivity, name: String, phoneNumber: String) {
        val contentResolver: ContentResolver = context.contentResolver

        val values = ContentValues().apply {
            put(ContactsContract.RawContacts.ACCOUNT_TYPE, null as String?)
            put(ContactsContract.RawContacts.ACCOUNT_NAME, null as String?)
        }

        val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
        val rawContactId = rawContactUri?.lastPathSegment?.toLongOrNull()

        val dataValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }

        contentResolver.insert(ContactsContract.Data.CONTENT_URI, dataValues)

        val phoneValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, "0${phoneNumber}")
            put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        }

        contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)
        Log.e("contact", "contact")
    }

    fun checkContactPermission(context: ComponentActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun isFormValid(): Boolean {
        val email = email.value
        val password = password.value

        val pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$".toRegex()

        var isEmailValid = false
        var isPasswordValid = false

        if (email != null){
            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        if (password.isNullOrEmpty()){
            isPasswordValid = false
        }else {
            isPasswordValid = pattern.matches(password.toString())
        }

        var validado = false

        if(isEmailValid && isPasswordValid) {
            Log.e("e", "algo")
            if (!userList.value.isNullOrEmpty()) {
                for (user in userList.value!!) {
                    Log.e("error email", "${user.email != email}")

                }
            } else validado = false
        } else validado = false



        return validado
    }

    fun registerStepOne(context: Context): LiveData<Boolean> {
        val email = email.value
        val password = password.value

        val pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$".toRegex()

        var isEmailValid = false
        var isPasswordValid = false

        if (email != null) {
            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        if (password.isNullOrEmpty()) {
            isPasswordValid = false
        } else {
            isPasswordValid = pattern.matches(password.toString())
        }

        if (isEmailValid && isPasswordValid) {
            val response = email?.let { repository.getUserStudentsByEmail(it) }
            val liveData = MutableLiveData<Boolean>()

            response?.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            liveData.postValue(true)

                        } else {
                            liveData.postValue(false)
                            showToast("Este correo ya existe", context)

                        }
                    } else {
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    showToast("Ha ocurrido un error, vuelva a intentarlo", context)

                }
            })

            return liveData
        } else {
            // Handle the case where email or password is not valid
            val invalidDataLiveData = MutableLiveData<Boolean>()
            invalidDataLiveData.value = false
            return invalidDataLiveData
        }
    }

    fun isCedulaValid(cedula: Int, context: Context): LiveData<Boolean> {


        if (cedula != null) {
            val response = cedula?.let { repository.getUserStudentsByCedula(it) }
            val liveData = MutableLiveData<Boolean>()

            response?.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            liveData.postValue(true)
                        } else {
                            liveData.postValue(false)
                            showToast("Esta cedula ya existe", context)

                        }
                    } else {
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    showToast("Ha ocurrido un error, vuelva a intentarlo", context)

                }
            })

            return liveData
        } else {
            // Handle the case where email or password is not valid
            val invalidDataLiveData = MutableLiveData<Boolean>()
            invalidDataLiveData.value = false
            return invalidDataLiveData
        }
    }



//    fun registerStepOne(context: Context): Boolean{
//        val email = email.value
//        val password = password.value
//
//        val pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$".toRegex()
//
//        var isEmailValid = false
//        var isPasswordValid = false
//
//        if (email != null){
//            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//        }
//
//        if (password.isNullOrEmpty()){
//            isPasswordValid = false
//        }else {
//            isPasswordValid = pattern.matches(password.toString())
//        }
//
////        var validado = false
//
//        if (isEmailValid && isPasswordValid){
//            val response = email?.let { repository.getUserStudentsByEmail(it) }
//            response?.enqueue(object : Callback<List<User>> {
//                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                    if (response.isSuccessful) {
//                        Log.e("Register Step one", "Hay un email")
//                        if(response.body()?.isEmpty() == true){
//                            registerStepOne.postValue(true)
//                            Log.e("email body succesful sin", response.body().toString())
//                        }else{
//                            registerStepOne.postValue(false)
//                            Log.e("email body sucessful con", response.body().toString())
//
//                        }
//
//                    } else {
//                        Log.e("Register Step one", "No hay un email")
//
//                        registerStepOne.postValue(false)
//
//                    }
//                }
//
//                override fun onFailure(call: Call<List<User>>, t: Throwable) {
//                    errorMessage.postValue(t.message)
//                }
//            })
//        }
//
//        Log.e("validado", registerStepOne.toString())
//
//        return registerStepOne as Boolean
//        return true
//
//
//    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    fun isUserValid(email: String, password: String): User? {
//        val userList = viewModel.userList.value ?: return null
//
//        // Iterate over the userList to check if there is a matching user with the given email and password
//        for (user in userList) {
//            if (user.email == email && user.password == password) {
//                viewModel.currentUser.value = user
//                return user
//            }
//        }
//
//        return null


    fun makeCall(number: Long, context: Context) {


        val phoneNumber = "1234567890" // Replace with the actual phone number you want to call
        val uri = Uri.parse("tel:0$number")
        val callIntent = Intent(Intent.ACTION_DIAL, uri)

        try {

            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            context.startActivity(callIntent)

        } catch (s: SecurityException) {

            // show() method display the toast with
            // exception message.
            Toast.makeText(context, "Un error ha ocurrido", Toast.LENGTH_LONG)
                .show()
        }
    }



    fun goToRegisterStepOne(activity: AppCompatActivity) {
        val fragment = RegisterStepOne()
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainActivity, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

//    fun goToRegisterStepTwo(context: RegisterStepOne) {
//        val intent = Intent(context, MainActivity::class.java)
//        intent.putExtra("fragment", "RegisterStepTwo")
//        context.startActivity(intent)
//    }

    fun navigateRegister() {

    }

    fun getLoggedIn(email: String, password:String, context: Context) {

        if (isOnline(context)){
            conexion.postValue(true)
            refreshingCurrentUser.postValue(true)
            Log.e("Logged in", "Logging In")

            repository.getLoggedIn(email, password).enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        //Checkear esto
//                    currentUser.postValue(response.body())
                        if(response.body()?.isNullOrEmpty() == true){

                            showToast("No se ha encontrado a ningun usuario", context)

                        }else{
                            val user = (response.body()?.get(0) ?: emptyList<User>()) as User?
                            currentUser.postValue((response.body()?.get(0) ?: emptyList<User>()) as User?)
                            currentUserCompose = (response.body()?.get(0) ?: emptyList<User>()) as User?

                            refreshingCurrentUser.postValue(false)
                            loggedIn.postValue(true)
                            createUserDB(UserDB(1, user?.id, user?.firstName, user?.lastName, user?.birthday, edad = user?.edad, cedula = user?.cedula, user?.gender, user?.imageProfile,user?.email, user?.password, user?.rol, user?.phone, user?.lgn, user?.lat, user?.created, user?.listActivities, user?.listOfMaterias))

                            Log.e("Logged in", "User: $user")
                            showToast("Has iniciado sesión correctamente", context)
                        }


                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingCurrentUser.postValue(false)
                        currentUserCompose = null
                        loggedIn.postValue(false)
                        Log.e("Logged in", "Fallo")

                        showToast("Ha ocurrido un error, verifica si tú contraseña o email son correctos", context)



                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    Log.e("Logged in fallo", "${t.message}")
                    showToast("Ha ocurrido un error, ${t.message}", context)


                }


            })

            val response = repository.getAllUsers()
            response.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        userList.postValue(response.body())
                        Log.e("Lista de usuariosss", response.body().toString())
                    } else {
                        Log.e("Lista de usuariosss", response.body().toString())
                        errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    Log.e("erorrrrrrr", "${t.message}")
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }


    }


    //Metodos Retrofit para users
    fun getAllUsers(context: Context) {
        if(isOnline(context)){
            conexion.postValue(true)
            val response = repository.getAllUsers()
            response.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        userList.postValue(response.body())
                        Log.e("Lista de usuariosss", response.body().toString())
                    } else {
                        Log.e("Lista de usuariosss", response.body().toString())
                        errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    Log.e("erorrrrrrr", "${t.message}")
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }


    }

    fun getUserStudents(rol: String, context: Context) {

        if (isOnline(context)){
            conexion.postValue(true)
            refreshing.postValue(true)
            val response = repository.getUserStudents(rol)
            response.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        userStudentsList.postValue(response.body())
                        Log.e("Lista de estudiantes list", response.body().toString())
                        refreshing.postValue(false)

                    } else {
                        Log.e("Lista fallida de estudiantes", response.body().toString())
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshing.postValue(false)

                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }


    }

    fun getAUserStudentsByFirstName(name: String, context: Context) {
        val response = repository.getUserStudentsByFirstName(name)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userStudentsList.postValue(response.body())
                    Log.e("Lista de estudiantes first name", response.body().toString())
                } else {
                    Log.e("Lista fallida de actividades", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }


    fun getUserStudentsByEmail(email: String, context: Context) {
        val response = repository.getUserStudentsByEmail(email)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userStudentsList.postValue(response.body())
                    Log.e("Lista de estudiantes email", response.body().toString())
                } else {
                    Log.e("Lista fallida de estudiantes email", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getAUserByGender(gender: String, context: Context) {
        val response = repository.getUserStudentsByGender(gender)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userStudentsList.postValue(response.body())
                    Log.e("Lista de estudiantes genero", response.body().toString())
                } else {
                    Log.e("Lista fallida de genero", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }


    //CREATE USER
//    fun createUser(user: User, context: Context) {
//        if(isOnline(context)){
//            conexion.postValue(true)
//            repository.createUser(user).enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    if (response.isSuccessful) {
//                        getAllUsers(context)
//                        getUserStudents("Estudiante", context)
//                        // Handle successful response
//                        Log.e("Create User", "User created successfully")
//                        showToast("Usuario creado exitosamente", context)
//
//                    } else {
//                        errorMessage.postValue("Error: ${response.code()}")
//                        showToast("Usuario no ha sido creado, vuelva a intentar", context)
//
//                    }
//                }
//
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    errorMessage.postValue(t.message)
//                }
//            })
//        }else{
//            conexion.postValue(false)
//            showToast("No hay conexion a internet", context)
//
//
//        }
//
//    }
//
//    //UPDATE USER
//    fun updateUser(userId: Int, user: User, context: Context) {
//        if (isOnline(context)){
//            conexion.postValue(true)
//            repository.updateUser(userId, user).enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    if (response.isSuccessful) {
//                        getAllUsers(context)
//                        getUserStudents("Estudiante", context)
//                        getUserById(userId, context)
//                        // Handle successful response
//                        Log.e("Update User", "User updated successfully")
//                        showToast("Usuario actualizado exitosamente", context)
//
//                    } else {
//                        errorMessage.postValue("Error: ${response.code()}")
//                        showToast("Usuario no ha sido actualizado, vuelva a intentar", context)
//
//                    }
//                }
//
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    errorMessage.postValue(t.message)
//                }
//            })
//        }else{
//            conexion.postValue(false)
//            showToast("No hay conexion a internet", context)
//
//
//        }
//
//    }
    fun getUpdatedList(listOfActivities: List<Actividad>, user: User): List<Actividad> {
        return listOfActivities.map { existingActivity ->
            user.listActivities?.find { it?.id == existingActivity.id } ?: existingActivity
        }
    }

    fun updateListWithActivitiesById(activityId: Int, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)) {
            conexion.postValue(true)
            refreshingActividadById.postValue(true)

            repository.getActivityByIdClass(activityId).enqueue(object : Callback<List<Actividad>> {
                override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                    if (response.isSuccessful) {
//                        activitiesListById.postValue(response.body())
//                        activitiesListByIdCompose = response.body()
                        Log.e("response", response.body().toString())
                        refreshingActividadById.postValue(false)
                        Log.e("is Working", "is Working")
                        val updatedList = response.body()?.map { existingActivity ->
                            val userActivity = updatedUser.value?.listActivities?.find { it?.id == existingActivity.id }
                            if (userActivity != null) {
                                // Replace properties based on conditions
                                existingActivity.copy(
                                    calification = userActivity.calification ?: existingActivity.calification,
                                    calificationRevision = userActivity.calificationRevision ?: existingActivity.calificationRevision,
                                    date = userActivity.date ?: existingActivity.date,
                                    description = userActivity.description ?: existingActivity.description,
                                    isCompleted = userActivity.isCompleted ?: existingActivity.isCompleted,
                                    messageStudent = userActivity.messageStudent ?: existingActivity.messageStudent,
                                    imageRevision = userActivity.imageRevision ?: existingActivity.imageRevision,
                                    title = userActivity.title ?: existingActivity.title
                                )
                            } else {
                                existingActivity
                            }
                        }

                        listOfActivitiesFiltered.value = updatedList
                        listOfActivitiesFilteredCompose = updatedList
                        Log.e("filtered updated List", listOfActivitiesFiltered.value.toString())
                        Log.e("filtered updated List compose", listOfActivitiesFilteredCompose.toString())

                        // Use the getUpdatedListAndCheckEmpty function
//                        val (isEmpty, updatedList) = getUpdatedListAndCheckEmpty(listOfActivities.value ?: emptyList(), updatedUserCompose ?: User())
//                        listOfActivities.postValue(updatedList)

//                        liveData.postValue(!isEmpty) // Return true if the updated list is not empty
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingActividadById.postValue(false)
                        liveData.postValue(false)
                    }
                }



                override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    refreshingActividadById.postValue(false)
                    liveData.postValue(false)
                }
            })
        } else {
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)
            liveData.postValue(false)
        }

        return liveData
    }

    fun createUser(user: User, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)) {
            conexion.postValue(true)
            repository.createUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        getAllUsers(context)
                        getUserStudents("Estudiante", context)
                        // Handle successful response
                        Log.e("Create User", "User created successfully")
                        showToast("Usuario creado exitosamente", context)
                        liveData.postValue(true)
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        showToast("Usuario no ha sido creado, vuelva a intentar", context)
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)
                }
            })
        } else {
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)
            liveData.postValue(false)
        }

        return liveData
    }

    // Create User


    // Update User
    fun updateUser(userId: Int, user: User, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)) {
            conexion.postValue(true)
            repository.updateUser(userId, user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        getAllUsers(context)
                        getUserStudents("Estudiante", context)
                        getUserById(userId, context)
                        // Handle successful response
                        Log.e("Update User", "User updated successfully")
                        showToast("Usuario actualizado exitosamente", context)
                        liveData.postValue(true)
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        showToast("Usuario no ha sido actualizado, vuelva a intentar", context)
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)
                }
            })
        } else {
            conexion.postValue(false)
            showToast("No hay conexión a internet", context)
            liveData.postValue(false)
        }

        return liveData
    }

    fun updateUserAsignacion(userId: Int, user: User, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)) {
            conexion.postValue(true)
            repository.updateUser(userId, user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        getAllUsers(context)
                        getUserStudents("Estudiante", context)
                        getUserById(userId, context)
                        // Handle successful response
                        Log.e("Update User", "User updated successfully")
                        liveData.postValue(true)
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)
                }
            })
        } else {
            conexion.postValue(false)
            showToast("No hay conexión a internet", context)
            liveData.postValue(false)
        }

        return liveData
    }

    fun updateUserList(userId: Int, user: User, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)) {
            conexion.postValue(true)
            repository.updateUser(userId, user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        getAllUsers(context)
                        getUserStudents("Estudiante", context)
                        getUserById(userId, context)
                        // Handle successful response
                        Log.e("Update User", "User updated successfully")
                        showToast("Usuario actualizado exitosamente", context)
                        liveData.postValue(true)
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        showToast("Usuario no ha sido actualizado, vuelva a intentar", context)
                        liveData.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)
                }
            })
        } else {
            conexion.postValue(false)
            showToast("No hay conexión a internet", context)
            liveData.postValue(false)
        }

        return liveData
    }

    //DELETE USER
    fun deleteUser(userId: Int, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)){
            conexion.postValue(true)
            repository.deleteUser(userId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        getAllUsers(context)
                        getUserStudents("Estudiante", context)
                        liveData.postValue(true)

                        // Handle successful response
                        Log.e("Delete User", "User deleted successfully")
                        showToast("Usuario eliminado exitosamente", context)

                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        showToast("Usuario no ha sido eliminado, vuelva a intentar", context)
                        liveData.postValue(false)


                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)

                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)
            liveData.postValue(false)

        }

        return liveData

    }

    fun getUserById(userId: Int, context: Context) {
        if (isOnline(context)){
            conexion.postValue(true)
            refreshingUpdatedUser.postValue(true)
            repository.getUserById(userId).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        //Checkear esto
//                    currentUser.postValue(response.body())
                        if (currentUser.value?.id == response.body()?.id || currentUserDB.value?.userId == response.body()?.id){
                            currentUser.postValue(response.body())
//                            currentUserDB.postValue(response.body() as UserDB)
                        }
                        updatedUser.postValue(response.body())
                        refreshingUpdatedUser.postValue(false)
                        updatedUserCompose = response.body()

                        val user = response.body()
                        Log.e("Get User by ID", "User: $user")
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingUpdatedUser.postValue(false)
                        updatedUser.postValue(null)
                        updatedUserCompose = null



                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }

    }

    fun getUserRefresh(userId: Int, context: Context) {

        if(isOnline(context)){
            refreshingCurrentUser.postValue(true)
            conexion.postValue(true)
            repository.getUserById(userId).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        //Checkear esto
                        refreshingCurrentUser.postValue(false)

                        currentUser.postValue(response.body())
                        currentUserCompose = response.body()
//                    updatedUser.postValue(response.body())
                        val user = response.body()
                        Log.e("Get User by ID", "User: $user")
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        currentUserCompose = null
                        refreshingCurrentUser.postValue(false)


                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    refreshingCurrentUser.postValue(false)

                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }

    }

    //Metodos retrofit para materias:

    fun getAllMaterias(context: Context) {
        if (isOnline(context)){
            conexion.postValue(true)
            refreshingMaterias.postValue(true)

            val response = repository.getAllMaterias()
            response.enqueue(object : Callback<List<Materia>> {
                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    if (response.isSuccessful) {
                        materiasList.postValue(response.body())
                        refreshingMaterias.postValue(false)

                        Log.e("Lista de usuarios", response.body().toString())
                    } else {
                        Log.e("Lista de usuarios", response.body().toString())
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingMaterias.postValue(false)

                    }
                }

                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)


        }

    }

    fun getMateriaById(materiaId: Int, context: Context) {
        if(isOnline(context)){
            conexion.postValue(true)
            refreshingMateriasById.postValue(true)

            repository.getMateriaById(materiaId).enqueue(object : Callback<Materia> {
                override fun onResponse(call: Call<Materia>, response: Response<Materia>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val user = response.body()
                        currentMateria.postValue(response.body())
                        refreshingMateriasById.postValue(false)

                        Log.e("Get User by ID", "User: $user")
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingMateriasById.postValue(false)

                    }
                }

                override fun onFailure(call: Call<Materia>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }

    }

    fun updateMateria(materiaId: Int, materia: Materia, context: Context) {
        if (isOnline(context)){
            conexion.postValue(true)

            repository.updateMateria(materiaId, materia).enqueue(object : Callback<Materia> {
                override fun onResponse(call: Call<Materia>, response: Response<Materia>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        Log.e("Update clase", "Clase updated successfully")
                        getAllMaterias(context)
                        getMateriaById(materiaId, context)
                        currentMateria.value?.id?.let { getMateriaById(it, context) }
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Materia>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)


        }

    }

    //Metodos retrofit para actividades:

    fun getAllActivities(context: Context) {
        if (isOnline(context)){
            conexion.postValue(true)
            refreshingActividad.postValue(true)

            val response = repository.getAllActivities()
            response.enqueue(object : Callback<List<Actividad>> {
                override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                    if (response.isSuccessful) {
                        activitiesList.postValue(response.body())
                        refreshingActividad.postValue(false)

                        Log.e("Lista de actividades", response.body().toString())
                    } else {
                        Log.e("Lista fallida de actividades", response.body().toString())
                        refreshingActividad.postValue(false)

                        errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }

    }

//    fun getActivityByIdClass(activityId: Int) {
//        repository.getActivityByIdClass(activityId).enqueue(object : Callback<Actividad> {
//            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
//                if (response.isSuccessful) {
//                    activitiesListById.postValue(response.body())
//                    val user = response.body()
//                    Log.e("Get User by ID", "User: $user")
//                } else {
//                    errorMessage.postValue("Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Actividad>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//        })
//    }

    fun getActivitiesById(activityId: Int, context: Context) {
        if(isOnline(context)){
            conexion.postValue(true)
            refreshingActividadById.postValue(true)

            val response = repository.getActivityByIdClass(activityId)
            response.enqueue(object : Callback<List<Actividad>> {
                override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                    if (response.isSuccessful) {
                        activitiesListById.postValue(response.body())
                        activitiesListByIdCompose = response.body()
                        refreshingActividadById.postValue(false)

                        Log.e("Lista de actividades", response.body().toString())
                    } else {
                        Log.e("Lista fallida de actividades", response.body().toString())
                        errorMessage.postValue("Error: ${response.code()}")
                        refreshingActividadById.postValue(true)

                    }
                }

                override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)

        }

    }

    fun createActivity(actividad: Actividad, context: Context) {

        if (isOnline(context)){
            conexion.postValue(true)
            repository.createActivity(actividad).enqueue(object : Callback<Actividad> {
                override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                    if (response.isSuccessful) {
                        getAllActivities(context)
                        getUserById(currentUser.value?.id ?: 10000, context)
                        currentMateria.value?.let { getActivitiesById(it.id, context) }
                        // Handle successful response

                        Log.e("Create Actividad", "Actividad created successfully")
                        showToast("Actividad creada exitosamente", context)

                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        showToast("Actividad no ha sido creada, vuelva a intentar", context)

                    }
                }

                override fun onFailure(call: Call<Actividad>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })

        }else{
            conexion.postValue(false)
            showToast("No hay conexion a internet", context)
        }

    }

    //UPDATE USER
    fun updateActivity(actividadId: Int, actividad: Actividad, context: Context) {
        repository.updateActivity(actividadId, actividad).enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    getAllActivities(context)
                    // Handle successful response
                    Log.e("Update Actividad", "Actividad updated successfully")

                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Actividad>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //DELETE USER
    fun deleteActivity(actividadId: Int, context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        if (isOnline(context)){
            conexion.postValue(true)

            repository.deleteActivity(actividadId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        liveData.postValue(true)
                        getAllActivities(context)
                        getActivitiesById(actividadId, context)
                        // Handle successful response
                        Log.e("Delete Actividad", "Actividad deleted successfully")
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                        liveData.postValue(false)

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    errorMessage.postValue(t.message)
                    liveData.postValue(false)

                }
            })
        }else{
            conexion.postValue(false)

        }

        return liveData
    }


    //Room
    fun getUsersDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var userDB = repository.getAllUsersDB()
                Log.e("Userd database ", "User in the database: ${userDB}")
            } catch (e: Exception) {
                Log.e("UserDatabase error", "Error getting user: ${e.message}")
            }
        }
    }

    fun getUserDB(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var userDB = repository.getUserByIdDB(userId)
                currentUserDB.postValue(userDB)
                currentUser.postValue(userDB as User)
                Log.e("User database", "User Database ${currentUserDB}")
            } catch (e: Exception) {
                Log.e("UserDatabase error", "Error user: ${e.message}")
            }
        }
    }

    //    fun UpdateUserDB(userId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                var userDB = repository.getUserByIdDB(userId)
//                currentUserDB.postValue(userDB)
//                Log.e("User database", "User Database ${currentUserDB}")
//            } catch (e: Exception) {
//                Log.e("UserDatabase error", "Error user: ${e.message}")
//            }
//        }
//    }
    fun createUserDB(user: UserDB) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.createUserDB(user)
                currentUserDB.postValue(user)

                Log.e("User database created", "User created")
            } catch (e: Exception) {
                Log.e("UserDatabase error", "Error creating user: ${e.message}")
            }
        }
    }
    fun deleteUserDB(user: UserDB) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteUserDB(user)
                Log.e("User database deleted", "User deleted")
            } catch (e: Exception) {
                Log.e("UserDatabase error", "Error creating user: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Perform cleanup tasks here, if needed
    }
}