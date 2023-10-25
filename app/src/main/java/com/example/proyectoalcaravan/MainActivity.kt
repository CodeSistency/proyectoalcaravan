package com.example.proyectoalcaravan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoalcaravan.databinding.ActivityMainBinding
import com.example.proyectoalcaravan.model.local.AppDatabase
import com.example.proyectoalcaravan.model.local.UserDao
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.repository.MainRepository
import com.example.proyectoalcaravan.viewmodels.LoginViewModel
import com.example.proyectoalcaravan.viewmodels.MyViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getDatabase(applicationContext).userDao()

        val repository = MainRepository(retrofitService, userDao)
        viewModel = ViewModelProvider(this, MyViewModelFactory(repository)).get(LoginViewModel::class.java)

        // Rest of your code
    }

    // Rest of your code
}