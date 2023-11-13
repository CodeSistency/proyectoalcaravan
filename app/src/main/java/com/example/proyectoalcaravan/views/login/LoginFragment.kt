package com.example.proyectoalcaravan.views.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentLoginBinding
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserDB(1)
        viewModel.getAllUsers()
        viewModel.getUserStudents("Estudiante")
        viewModel.getAllMaterias()
        viewModel.getAllActivities()

        if(viewModel.userList?.value?.isNullOrEmpty() == true){
            Log.e("no hay", "no hay")
            binding.loginBtn.isEnabled = false
        }

        binding.loginBtn.setOnClickListener{
            viewModel.getAllUsers()

            val email = binding.etEmail.text.toString()
            val password = binding.password.text.toString()
            if (isOnline(requireContext())){
                val isUserValid = isUserValid(email, password)

                if (isUserValid != null) {


                    viewModel.createUserDB(UserDB(1, isUserValid.id, isUserValid.firstName, isUserValid.lastName, isUserValid.birthday, isUserValid.cedula, isUserValid.gender, isUserValid.imageProfile, isUserValid.email, isUserValid.password, isUserValid.rol, isUserValid.phone, isUserValid.lgn, isUserValid.lat, isUserValid.listActivities, isUserValid.listOfMaterias))


                    if (isUserValid.rol == "Estudiante"){
                        view.findNavController().navigate(R.id.action_login_to_studentFragment)
                    } else {
                        view.findNavController().navigate(R.id.action_login_to_profesorFragment)

                    }
                } else {
                    Toast.makeText(requireContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show()

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