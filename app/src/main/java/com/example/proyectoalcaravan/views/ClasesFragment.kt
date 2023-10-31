package com.example.proyectoalcaravan.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.Header


class ClasesFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllMaterias()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ClasesContent()
            }
        }
    }

@Composable
fun ListItem(item: Materia) {
    // Replace with your list item implementation
    // You can use Card, ListItem, or any other Composable to represent a single item in the list
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable {
                viewModel.currentMateria.postValue(item)
                view
                    ?.findNavController()
                    ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
            },
        elevation = 4.dp,

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item.name,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(bottom = 2.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold

                    )
                Text(text = "Inscritos: ${item.listStudent?.size}",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 2.dp),
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )
            }
//
//            Text(text = item.id,
//            modifier = Modifier.padding(16.dp),
//                color = Color.LightGray
//            )
            Text(text = item.id.toString())
        }

    }
}

    @Composable
    fun ListContent(materiasList: MutableLiveData<List<Materia>>) {
        val materias = materiasList.value ?: emptyList()

        LazyColumn {
            items(materias) { materia ->
                ListItem(item = materia)
            }
        }
    }


    @Composable
    fun ClasesContent() {
        Column {
            Header(titulo = "Materias")
            ListContent(materiasList = viewModel.materiasList)
        }

    }
}