package com.example.proyectoalcaravan.views.compose

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.compose.profesor.ProfesorContent
import com.example.proyectoalcaravan.views.compose.student.StudentContent


class ComposeNavigationFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentLifecycle", "Fragment created: ${javaClass.simpleName}")
        Log.e("compose fragment", "compose fragment onCreated")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            Log.e("compose fragment", "compose fragment onViewCreated")
            setContent {
                Nav()

            }
        }
    }

    @Composable
    fun Nav (){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(
                    onNavigateToProfesor = {
                    navController.navigate("professor")
                                       },

                    onNavigateToStudent = {
                    navController.navigate("student")
                                      },
                )
            }

            composable("professor") { ProfesorContent(/*...*/) }

            composable("student") { StudentContent(/*...*/) }
            /*...*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("compose fragment", "compose fragment destroy")

    }

}