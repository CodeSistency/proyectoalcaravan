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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.yml.charts.common.extensions.isNotNull
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentLoginBinding
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

         val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that that your app will not show notifications.
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
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.
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

            viewModel.loggedIn.observe(viewLifecycleOwner){ it ->
                binding.loginBtn.isEnabled = true

                if (it){
                    binding.loginBtn.isEnabled = true
                    viewModel.currentUser.observe(viewLifecycleOwner){
                        if (it.rol == "Estudiante"){
                            binding.loginBtn.isEnabled = true

                            if(view.findNavController().isNotNull()){
                                view.findNavController().navigate(R.id.studentFragment)
                                viewModel.getUserDB(1)
                                binding.loginBtn.isEnabled = true

                            }
                    } else {
                            binding.loginBtn.isEnabled = true

                            if (view.findNavController().isNotNull())
                        view.findNavController().navigate(R.id.profesorFragment)
                            viewModel.getUserDB(1)
                            binding.loginBtn.isEnabled = true



                        }
                    }
//                    view.findNavController().navigate(R.id.action_login_to_studentFragment)
                }
            }






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