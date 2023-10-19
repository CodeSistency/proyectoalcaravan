package com.example.proyectoalcaravan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoalcaravan.databinding.ActivityMainBinding
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.repository.MainRepository
import com.example.proyectoalcaravan.viewmodels.LoginViewModel
import com.example.proyectoalcaravan.viewmodels.MyViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: LoginViewModel

    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
                LoginViewModel::class.java
            )

        binding.loginBtn.setOnClickListener{
            viewModel.getAllUsers()
        }
    }
}