package com.example.proyectoalcaravan.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.views.componentes.Header


class AsignacionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsignacionContent()

            }
        }
    }


    @Composable
    fun AsignacionContent(){
        Header(titulo = "Asignacion")

    }


}