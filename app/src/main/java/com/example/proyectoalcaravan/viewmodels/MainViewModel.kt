package com.example.proyectoalcaravan.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.example.proyectoalcaravan.views.profesor.ProfesorActivity
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.RegisterStepOne
import com.example.proyectoalcaravan.views.student.StudentActivity
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository): ViewModel() {

    //Estados actuales
    var currentUser = MutableLiveData<User>()
    var currentMateria = MutableLiveData<Materia>()
    var currentActividad = MutableLiveData<Actividad>()
    var updatedUser = MutableLiveData<User>()

    //Listas de datos
    var userList = MutableLiveData<List<User>>()
    var userStudentsList = MutableLiveData<List<User>>()
    var materiasList = MutableLiveData<List<Materia>>()
    var materiasUserList = MutableLiveData<List<Materia>>()
    var activitiesList = MutableLiveData<List<Actividad>>()
    var activitiesListById = MutableLiveData<List<Actividad>>()

    val errorMessage = MutableLiveData<String>()

    //Data to Register

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val birthday = MutableLiveData<String>("Fecha")
    val rol = MutableLiveData<String>()
    val genero = MutableLiveData<String>()
    val listOfActivities = MutableLiveData<List<Actividad?>>(listOf())



    val profileImage = MutableLiveData<Uri>()

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
            if (!userList.value.isNullOrEmpty()) {
                for (user in userList.value!!) {
                    validado = user.email != email
                }
            } else validado = true
        } else validado = false



        return validado
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
        val uri = Uri.parse("tel:$number")
        val callIntent = Intent(Intent.ACTION_DIAL, uri)

        try {

            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            context.startActivity(callIntent)

        } catch (s: SecurityException) {

            // show() method display the toast with
            // exception message.
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun goToActivity(context: Context) {
        val intent = Intent(context, StudentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun goToStudent(context: Context) {
        val intent = Intent(context, StudentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun goToProfesor(context: Context) {
        val intent = Intent(context, ProfesorActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
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


 //Metodos Retrofit para users
    fun getAllUsers() {

        val response = repository.getAllUsers()
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                    Log.e("Lista de usuarios", response.body().toString())
                } else {
                    Log.e("Lista de usuarios", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getAUserStudents(rol: String) {
        val response = repository.getUserStudent(rol)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userStudentsList.postValue(response.body())
                    Log.e("Lista de actividades", response.body().toString())
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

    //CREATE USER
    fun createUser(user: User) {
        repository.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    getAllUsers()
                    // Handle successful response
                    Log.e("Create User", "User created successfully")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //UPDATE USER
    fun updateUser(userId: Int, user: User) {
        repository.updateUser(userId, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    getAllUsers()
                    // Handle successful response
                    Log.e("Update User", "User updated successfully")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //DELETE USER
    fun deleteUser(userId: Int) {
        repository.deleteUser(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getAllUsers()
                    // Handle successful response
                    Log.e("Delete User", "User deleted successfully")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getUserById(userId: Int) {
        repository.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    updatedUser.postValue(response.body())
                    val user = response.body()
                    Log.e("Get User by ID", "User: $user")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //Metodos retrofit para materias:

    fun getAllMaterias() {
        val response = repository.getAllMaterias()
        response.enqueue(object : Callback<List<Materia>> {
            override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                if (response.isSuccessful) {
                    materiasList.postValue(response.body())
                    Log.e("Lista de usuarios", response.body().toString())
                } else {
                    Log.e("Lista de usuarios", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getMateriaById(materiaId: Int) {
        repository.getMateriaById(materiaId).enqueue(object : Callback<Materia> {
            override fun onResponse(call: Call<Materia>, response: Response<Materia>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val user = response.body()
                    Log.e("Get User by ID", "User: $user")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Materia>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun updateMateria(materiaId: Int, materia: Materia) {
        repository.updateMateria(materiaId, materia).enqueue(object : Callback<Materia> {
            override fun onResponse(call: Call<Materia>, response: Response<Materia>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Log.e("Update clase", "Clase updated successfully")
                    getAllMaterias()
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Materia>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //Metodos retrofit para actividades:

    fun getAllActivities() {
        val response = repository.getAllActivities()
        response.enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                if (response.isSuccessful) {
                    activitiesList.postValue(response.body())
                    Log.e("Lista de actividades", response.body().toString())
                } else {
                    Log.e("Lista fallida de actividades", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
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

    fun getActivitiesById(activityId: Int) {
        val response = repository.getActivityByIdClass(activityId)
        response.enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                if (response.isSuccessful) {
                    activitiesListById.postValue(response.body())
                    Log.e("Lista de actividades", response.body().toString())
                } else {
                    Log.e("Lista fallida de actividades", response.body().toString())
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun createActivity(actividad: Actividad) {
        repository.createActivity(actividad).enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    getAllActivities()
                    // Handle successful response
                    Log.e("Create Actividad", "Actividad created successfully")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Actividad>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    //UPDATE USER
    fun updateActivity(actividadId: Int, actividad: Actividad) {
        repository.updateActivity(actividadId, actividad).enqueue(object : Callback<Actividad> {
            override fun onResponse(call: Call<Actividad>, response: Response<Actividad>) {
                if (response.isSuccessful) {
                    getAllActivities()
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
    fun deleteActivity(actividadId: Int) {
        repository.deleteActivity(actividadId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getAllActivities()
                    // Handle successful response
                    Log.e("Delete Actividad", "Actividad deleted successfully")
                } else {
                    errorMessage.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }


    //Room
    fun createUserDB(user: UserDB) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.createUserDB(user)
            } catch (e: Exception) {
                Log.e("UserDatabase error", "Error creating user: ${e.message}")
            }
        }
    }
}