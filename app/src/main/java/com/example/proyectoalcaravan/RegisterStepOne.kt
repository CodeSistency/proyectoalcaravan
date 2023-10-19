package com.example.proyectoalcaravan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoalcaravan.databinding.FragmentRegisterStepOneBinding
import com.example.proyectoalcaravan.model.User
import com.example.proyectoalcaravan.viewmodels.LoginViewModel


class RegisterStepOne : Fragment() {
    private var _binding: FragmentRegisterStepOneBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.let {
//            viewModel = it.getParcelable("loginViewModel")!!
//        }

        binding.registerStepOneButton.setOnClickListener {
            val email = binding.emailTextInputLayout.editText?.text.toString()
            val password = binding.passwordTextInputLayout.editText?.text.toString()

            val user = User(email = email, password = password)

            viewModel.createUser(user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}