package com.example.proyectoalcaravan.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavInflater
import androidx.navigation.findNavController
import co.yml.charts.common.extensions.isNotNull
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.ActivityMainBinding
import com.example.proyectoalcaravan.model.local.AppDatabase
import com.example.proyectoalcaravan.model.local.UserDao
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.repository.MainRepository
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.viewmodels.MyViewModelFactory
import com.example.proyectoalcaravan.views.profesor.ProfesorFragment
import com.example.proyectoalcaravan.views.student.StudentFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var userDao: UserDao
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getDatabase(applicationContext).userDao()

        Log.e("activity", "activity")
        val repository = MainRepository(retrofitService, userDao)
        viewModel = ViewModelProvider(this, MyViewModelFactory(repository)).get(MainViewModel::class.java)
        viewModel.getUserDB(1)
        viewModel.getAllUsers(this)
        viewModel.getAllActivities(this)
        viewModel.getAllMaterias(this)
        viewModel.getUserStudents("Estudiante", this)


        if (viewModel.currentUserDB.value != null && viewModel.currentUserDB?.value!!.rol.isNotNull()) {
            if (viewModel.currentUserDB.value!!.rol == "Estudiante") {
                // Navigate to the StudentFragment
                Log.e("something", "algo")
                navController = findNavController(R.id.nav_host_fragment)
//                navController.navigate(R.id.studentFragment)
                navController.navigate(R.id.studentFragment)
//                navController.navigate(R.id.composeNavigationFragment)


//                    Log.e("nav", navController.graph.toString())
//
            } else if (viewModel.currentUserDB.value!!.rol == "Profesor") {
                // Navigate to the ProfessorFragment
                navController = findNavController(R.id.nav_host_fragment)
//                navController.navigate(R.id.profesorFragment)
                navController.navigate(R.id.profesorFragment)
//                navController.navigate(R.id.composeNavigationFragment)



                Log.e("nav", navController.graph.toString())
                Log.e("something", "algo")



            }
        }
        Log.e("database", viewModel.currentUserDB.value.toString())

        viewModel.currentUserDB.observe(this) { user ->
            Log.e("database2", user.toString())

            Log.e("something", "algo $user")

            // Check if the user exists and has a non-null and non-empty 'rol' property
            if (user != null && user.rol.isNotNull()) {
                if (user.rol == "Estudiante") {
                    // Navigate to the StudentFragment
                    Log.e("something", "algo")
                    navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.studentFragment)
//                    navController.navigate(R.id.composeNavigationFragment)


//                    startActivity(Intent(this, MainActivity::class.java))
                } else if (user.rol == "Profesor") {
                    Log.e("something", "algo")

                    // Navigate to the ProfessorFragment
                    navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.profesorFragment)
//                    navController.navigate(R.id.composeNavigationFragment)


                    Log.e("nav", navController.graph.toString())


                }
            }
        }


    }

}