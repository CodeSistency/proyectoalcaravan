package com.example.proyectoalcaravan.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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

class SplashScreen : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var userDao: UserDao
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        userDao = AppDatabase.getDatabase(applicationContext).userDao()

        val repository = MainRepository(retrofitService, userDao)
        viewModel = ViewModelProvider(this, MyViewModelFactory(repository)).get(MainViewModel::class.java)

        viewModel.getUserDB(1)

        viewModel.currentUserDB.observe(this, Observer { user ->
            // Check if the user exists and has a non-null and non-empty 'rol' property
            if (user != null && user.rol.isNotNull()) {
                if (user.rol == "Estudiante") {
                    // Navigate to the StudentFragment
                    navController = findNavController(R.id.nav_host_fragment)

                    val navGraph = navController.graph
                    navGraph.setStartDestination(R.id.studentFragment)
                    navController.setGraph(navGraph.id)

                    println(navController.graph)

//                    navController.setGraph(navGraph)

//                    val navGraph = navController.getNavigationGraph()
//                    val navInflater = NavInflater.inflate(this, navController.getNavInflater())
////                    val navGraph = navInflater.inflate(navController.navGraph.id)
//
//                    navGraph.setStartDestination(R.id.profesorFragment)
//                    navController.setGraph(navGraph)
                    startActivity(Intent(this, StudentFragment::class.java))
                } else if (user.rol == "Profesor") {
                    // Navigate to the ProfessorFragment

                    val navGraph = navController.graph
                    navGraph.setStartDestination(R.id.profesorFragment)
                    navController.setGraph(navGraph.id)

                    println(navController.graph)

                    startActivity(Intent(this, ProfesorFragment::class.java))
                }
            } else {
                // User doesn't exist or has an empty 'rol'
                // Handle this case, e.g., show an error message or go to a default screen
                // For now, we just navigate to the MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        })

        // Optionally, you can add a timeout to prevent waiting indefinitely for the user data
        Handler().postDelayed({
            // If the user data is not available within the specified time, navigate to the MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }
}