package com.example.proyectoalcaravan.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.Header
import com.example.proyectoalcaravan.views.componentes.Header2


class AsignacionFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    val args:AsignacionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            viewModel.getUserById(args.student)
            setContent {
                AsignacionContent()

            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacion(item: Actividad) {
        // Replace with your list item implementation
        var isModalNotaVisible by remember { mutableStateOf(false) }
        var notaAsignacion by remember { mutableStateOf(String()) }



        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
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
                    Text(text = item.title,
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.padding(bottom = 2.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold

                    )
                    Text(text = "Fecha: ${item.date}",
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
                if (item.isCompleted){
                    Text(text = item.calificationRevision.toString())
                }else{
                    Column {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
                        }
                        Text(text = "Por entregar")
                    }

                }


            }

        }

        if (isModalNotaVisible) {

            Dialog(
                onDismissRequest = { isModalNotaVisible = false },
                content = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(245, 245, 245),
                                shape = RoundedCornerShape(16.dp),
                            )
                            .border(1.dp, Color.Gray)
                            .drawWithContent {
                                drawContent()

//                                    drawShadow(16.dp, shape = RoundedCornerShape(16.dp))
                            },
                        elevation = 8.dp
                    ) {
                        if (item.imageRevision.isNullOrEmpty()){
                            Text(text = "Todavía no se ha enviado la asignación")
                        }else{
                            Column {
                                GlideImage(
                                    model = item.imageRevision,
                                    contentDescription = "foto",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)  )
                                Column {
                                    Text(text = "Calificación")
                                    OutlinedTextField(value = notaAsignacion,
                                        onValueChange = {
                                            if (it.all { char -> char.isDigit() }) {
                                                notaAsignacion = it
                                            }
                                            },

                                        placeholder = {
                                            Text(text = "Coloque la nota:")
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )

                                    )
                                    Button(onClick = {  }

                                    ) {
                                        Text(text = "Enviar nota")
                                    }
                                }
                            }
                        }



//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                    text = "Agregar Asignación",
//                                    style = TextStyle(fontSize = 24.sp)
//                                )
//                                Spacer(modifier = Modifier.weight(1F))
//                                Icon(
//                                    imageVector = Icons.Default.Close,
//                                    contentDescription = null,
//                                    modifier = Modifier.clickable { isModalAsignacionesVisible = false }
//                                )
//                            }
//
//                            Column {
//
//                                Text(text = "Titulo")
//                                OutlinedTextField(value = tituloAsignacion,
//                                    onValueChange = { tituloAsignacion = it},
//                                    placeholder = {
//                                        Text(text = "Nombre de la actividad")
//                                    }
//                                )
//
//                                Text(text = "Descripción")
//                                OutlinedTextField(value = descripcionAsignacion,
//                                    onValueChange = {descripcionAsignacion = it},
//                                    placeholder = {
//                                        Text(text = "Describa la actividad")
//                                    }
//                                )
//
//                                Button(onClick = {
//                                    val newFragment = DatePickerFragment()
//                                    newFragment.show(childFragmentManager, "datePicker")
//                                }) {
//                                    Text(text = "Fecha")
//                                }
//                            }
//
//
//                            Button(
//                                onClick = { viewModel.createActivity(
//                                    Actividad(
//                                        idClass = viewModel.currentMateria.value!!.id,
//                                        title = tituloAsignacion,
//                                        description = descripcionAsignacion,
//                                        date = viewModel.birthday.value,
//
//                                        )
//                                )},
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            ) {
//                                Text(text = "Actualizar")
//                            }
//                        }
                    }
                }
            )
        }
    }

    @Composable
    fun ListContentAsignacion(listOfActivities: MutableLiveData<List<Actividad>>) {
        val actividades by listOfActivities.observeAsState(initial = emptyList())

        LazyColumn {
            items(actividades) { actividad ->
                ListItemAsignacion(item = actividad)
            }
        }
    }

    @Composable
    fun AsignacionContent(){
        Column {
            Header2(titulo = "Asignaciones", viewModel.currentMateria?.value?.name ?: "", )
            ListContentAsignacion(listOfActivities = viewModel.activitiesListById)
        }

    }


}