package com.example.proyectoalcaravan.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalcaravan.ProfesorActivity
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.RegisterStepOne
import com.example.proyectoalcaravan.StudentActivity
import com.example.proyectoalcaravan.model.local.AppDatabase
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: MainRepository): ViewModel() {



    val userList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()

    //Data to Register

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()


    fun isFormValid(): Boolean {
        val email = email.value
        val password = password.value

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password?.length ?: 0 >= 5 && password?.any { it.isDigit() } ?: false

        return isEmailValid && isPasswordValid
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



    fun getAllUsers() {

        Log.e("Lista de usuarios", "fmksdkfp")
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

    //CREATE USER
    fun createUser(user: User) {
        repository.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
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
    
    //Room
    fun createUserDB(user: UserDB) {
        viewModelScope.launch {
            val pr= User()
//            UserDB()...Rellenar datos, para pasar de user a userdb
//            repository.userDao.insertUser(user)
        }
    }
}