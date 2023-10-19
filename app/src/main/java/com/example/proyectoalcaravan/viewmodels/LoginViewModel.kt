package com.example.proyectoalcaravan.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.RegisterStepOne
import com.example.proyectoalcaravan.StudentActivity
import com.example.proyectoalcaravan.model.User
import com.example.proyectoalcaravan.model.UserList
import com.example.proyectoalcaravan.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: MainRepository): ViewModel() {
    val userList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()

    fun goToActivity(context: Context) {
        val intent = Intent(context, StudentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun goToFragment(activity: AppCompatActivity) {
        val fragment = RegisterStepOne()
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainActivity, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getAllUsers() {
        val response = repository.getAllUsers()
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                    Log.e("Lista de usuarios", response.body().toString())
                } else {
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
}