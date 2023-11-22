package com.example.proyectoalcaravan.views.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepOneBinding
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel


class RegisterStepOne : Fragment() {
    private var _binding: FragmentRegisterStepOneBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllUsers(requireContext())

        binding.editTextEmail?.doOnTextChanged { text, _, _, _ ->
            viewModel.email.value = text?.toString()
            Log.e("email", text.toString())
        }

        binding.editTextPassword?.doOnTextChanged { text, _, _, _ ->
            viewModel.password.value = text?.toString()
            Log.e("contraseña", text.toString())
        }

        binding.registerStepOneButton.setOnClickListener{
            if (isOnline(requireContext())){
                binding.simpleProgressBarRegisterStepOne.visibility = View.VISIBLE
//                viewModel.getAllUsers(requireContext())

//                    if(viewModel.isFormValid()){
                viewModel.registerStepOne(requireContext()).observe(viewLifecycleOwner){
                    if (it){
                        binding.simpleProgressBarRegisterStepOne.visibility = View.GONE

                        view.findNavController().navigate(R.id.action_registerStepOne2_to_registerStepTwo2)

                     }else{
                        binding.simpleProgressBarRegisterStepOne.visibility = View.GONE

                        Toast.makeText(requireContext(), "Email o clave invalida", Toast.LENGTH_SHORT).show()

                }
                     }
//                    if(viewModel.registerStepOne(requireContext()) == true){
//
//                        view.findNavController().navigate(R.id.action_registerStepOne2_to_registerStepTwo2)
//                    }else{
//                        Toast.makeText(requireContext(), "Email o clave invalida", Toast.LENGTH_SHORT).show()
//                    }

            }else{
                viewModel.showToast("Es necesario el acceso a internet", requireContext())


            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}