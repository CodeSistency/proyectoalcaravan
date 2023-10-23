package com.example.proyectoalcaravan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepTwoBinding
import com.example.proyectoalcaravan.viewmodels.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterStepTwo.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterStepTwo : Fragment() {
    private var _binding: FragmentRegisterStepTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.let {
//            viewModel = it.getParcelable("loginViewModel")!!
//        }

        //Data


//        binding.Registrar.setOnClickListener {
//            // Access the values from the ViewModel
//            val email = viewModel.email.value
//            val password = viewModel.password.value
//
//            val nombre = binding.etNombre.text.toString()
//            val apellido = binding.etApellido.text.toString()
//            val cedula = binding.etCedula.text.toString().toIntOrNull()
//            val telefono = binding.etTelefono.text.toString().toIntOrNull()
//            val foto = binding.etFoto.text.toString()
//
//
//
//            if (email != null && password != null) {
//                val user = User(email = email, password = password, firstName = nombre, lastName = apellido, cedula = cedula, phone = telefono, imageProfile = foto, birthday = "a", )
//                viewModel.createUser(user)
//            } else {
//                // Handle the case when the values are not available
//            }
//        }

        binding.btnUbicacion.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerStepTwo2_to_googleMapsFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}