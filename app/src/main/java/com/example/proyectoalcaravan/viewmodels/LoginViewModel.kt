package com.example.proyectoalcaravan.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectoalcaravan.model.User
import com.example.proyectoalcaravan.model.UserList
import com.example.proyectoalcaravan.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: MainRepository): ViewModel() {
    val userList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllUsers() {
        val response = repository.getAllUsers()
        response.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                userList.postValue(response.body()?.userList)
                Log.e("Lista de usuarios", response.toString())
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}