package com.example.proyectoalcaravan.views.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.yml.charts.common.extensions.isNotNull
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentLoginBinding
import com.example.proyectoalcaravan.model.local.AppDatabase
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.local.UserDao
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.repository.MainRepository
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.viewmodels.MyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
//    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var navController: NavController
//    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var userDao: UserDao
    private val viewModel by activityViewModels<MainViewModel>()


//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            Log.d("FragmentLifecycle", "Fragment created: ${javaClass.simpleName}")

////        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
////        viewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(repository)).get(MainViewModel::class.java)
////        viewModel = ViewModelProvider(navHostFragment, MyViewModelFactory(repository)).get(MainViewModel::class.java)
//
//        viewModel.getUserDB(1)
//        viewModel.getAllUsers(requireContext())
//        viewModel.getAllActivities(requireContext())
//        viewModel.getAllMaterias(requireContext())
//        viewModel.getUserStudents("Estudiante", requireContext())



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        if (viewModel.currentUserDB.value != null && viewModel.currentUserDB?.value!!.rol.isNotNull()) {
//            if (viewModel.currentUserDB.value!!.rol == "Estudiante") {
//                // Navigate to the StudentFragment
//                Log.e("something", "algo")
//                navController = view.findNavController()
////                navController.navigate(R.id.studentFragment)
//                navController.navigate(R.id.studentFragment)
//
////                    Log.e("nav", navController.graph.toString())
////
//            } else if (viewModel.currentUserDB.value!!.rol == "Profesor") {
//                // Navigate to the ProfessorFragment
//                navController = findNavController()
////                navController.navigate(R.id.profesorFragment)
//                navController.navigate(R.id.profesorFragment)
//
//
//                Log.e("nav", navController.graph.toString())
//                Log.e("something", "algo")
//
//
//
//            }
//        }
//        Log.e("database", viewModel.currentUserDB.value.toString())
//
//        viewModel.currentUserDB.observe(viewLifecycleOwner) { user ->
//            Log.e("database2", user.toString())
//
//            Log.e("something", "algo $user")
//
//            // Check if the user exists and has a non-null and non-empty 'rol' property
//            if (user != null && user.rol.isNotNull()) {
//                if (user.rol == "Estudiante") {
//                    // Navigate to the StudentFragment
//                    Log.e("something", "algo")
//                    navController = view.findNavController()
//                    navController.navigate(R.id.studentFragment)
//
////                    startActivity(Intent(this, MainActivity::class.java))
//                } else if (user.rol == "Profesor") {
//                    Log.e("something", "algo")
//
//                    // Navigate to the ProfessorFragment
//                    navController = findNavController()
//                    navController.navigate(R.id.profesorFragment)
//
//                    Log.e("nav", navController.graph.toString())
//
//
//                }
//            }
//        }

         val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
            }
        }

       fun askNotificationPermission() {
            // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        askNotificationPermission()

        viewModel.getUserDB(1)
        viewModel.getAllUsers(requireContext())
        viewModel.getUserStudents("Estudiante", requireContext())
        viewModel.getAllMaterias(requireContext())
        viewModel.getAllActivities(requireContext())

//        if(viewModel.userList?.value?.isNullOrEmpty() == true){
//            Log.e("no hay", "no hay")
//            binding.loginBtn.isEnabled = false
//        }

        binding.loginBtn.setOnClickListener{
            binding.loginBtn.isEnabled = false

            // Delay in milliseconds (adjust as needed)
//            val delayMillis: Long = 2000
//
//            // Use a Handler to enable the button after the delay
//            GlobalScope.launch(Dispatchers.Main) {
//                delay(delayMillis)
//                // Enable the button after the delay
//                binding.loginBtn.isEnabled = true
//            }
            viewModel.getAllUsers(requireContext())

            val email = binding.etEmail.text.toString()
            val password = binding.password.text.toString()

            viewModel.getLoggedIn(email, password, requireContext())
            binding.simpleProgressBarLogin.visibility = View.VISIBLE

            viewModel.loggedIn.observe(viewLifecycleOwner){ it ->
                binding.loginBtn.isEnabled = true

                if (it){
                    binding.loginBtn.isEnabled = true
                    binding.simpleProgressBarLogin.visibility = View.GONE

                    viewModel.currentUser.observe(viewLifecycleOwner){
                        if (it.rol == "Estudiante"){
                            binding.loginBtn.isEnabled = true
                            binding.simpleProgressBarLogin.visibility = View.GONE


                            if(view.findNavController().isNotNull()){
                                view.findNavController().navigate(R.id.studentFragment)
//                                view.findNavController().navigate(R.id.composeNavigationFragment)
                                binding.simpleProgressBarLogin.visibility = View.GONE

                                viewModel.getUserDB(1)
                                binding.loginBtn.isEnabled = true

                            }
                    } else {
                            binding.loginBtn.isEnabled = true
                            binding.simpleProgressBarLogin.visibility = View.GONE

                            if (view.findNavController().isNotNull())
                        view.findNavController().navigate(R.id.profesorFragment)
                            binding.simpleProgressBarLogin.visibility = View.GONE

//                                view.findNavController().navigate(R.id.composeNavigationFragment)
                            viewModel.getUserDB(1)
                            binding.loginBtn.isEnabled = true
                        }
                    }
//                    view.findNavController().navigate(R.id.action_login_to_studentFragment)
                }else{
                    binding.simpleProgressBarLogin.visibility = View.GONE
                    binding.loginBtn.isEnabled = true

                }
            }

//            viewModel.loggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
//                binding.loginBtn.isEnabled = true
//
//                if (isLoggedIn) {
//                    // Remove the observer to prevent multiple triggers
//                    viewModel.loggedIn.removeObservers(viewLifecycleOwner)
//
//                    viewModel.currentUser.observe(viewLifecycleOwner) { user ->
//                        if (user != null) {
//                            if (user.rol == "Estudiante") {
//                                // Navigate only if the current destination is not the target destination
//                                val currentDestination = view.findNavController().currentDestination?.id
//                                if (currentDestination != R.id.composeNavigationFragment) {
//                                    view.findNavController().navigate(R.id.composeNavigationFragment)
//                                    viewModel.getUserDB(1)
//                                }
//                            } else if(user.rol == "Profesor"){
//                                val currentDestination = view.findNavController().currentDestination?.id
//                                if (currentDestination != R.id.composeNavigationFragment) {
//                                    view.findNavController().navigate(R.id.composeNavigationFragment)
//                                    viewModel.getUserDB(1)
//                                }
//                                // Handle navigation for other roles
//                                // view.findNavController().navigate(R.id.action_login_to_anotherFragment)
//                            }
//                        }
//                    }
//                }
//            }






        }

        binding.newAccount.setOnClickListener{
            view.findNavController().navigate(R.id.action_login_to_registerStepOne2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isUserValid(email: String, password: String): User? {
        val userList = viewModel.userList.value ?: return null

        // Iterate over the userList to check if there is a matching user with the given email and password
        for (user in userList) {
            if (user.email == email && user.password == password) {
                viewModel.currentUser.value = user
                return user
            }
        }

        return null
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}