package com.example.proyectoalcaravan.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.Header

class MateriaFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            viewModel.getAllMaterias()
            viewModel.getAllUsers()
            setContent {
                MateriaContent()

            }
        }
    }

    @Composable
    fun ListContentUsers(userList: MutableLiveData<List<User>>, selectedUsers: MutableList<User>) {
        val users by userList.observeAsState(initial = emptyList())
//        val selectedUsers = remember { mutableStateListOf<User>() } // Track selected users

        LazyColumn {
            items(users) { user ->
                ListItemUser(user, selectedUsers)
            }
        }

        // You can use selectedUsers for your further processing.
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemUser(user: User, selectedUsers: MutableList<User>) {
        var isModalVisible by remember { mutableStateOf(false) }
        var isChecked by remember { mutableStateOf(false) } // Checkbox state

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium),
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular user image
                GlideImage(
                    model = user.imageProfile,
                    contentDescription = "foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)  )

                Spacer(modifier = Modifier.width(16.dp))

                        Column(
                        modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = user.firstName.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = user.lastName.toString(),
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                }

                // Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newCheckedState ->
                        isChecked = newCheckedState
                        if (isChecked) {
                            selectedUsers.add(user) // Add the user to the selectedUsers list
                            Log.e("user added", "${user.toString()}")
                        } else {
                            selectedUsers.remove(user) // Remove the user from the selectedUsers list
                            Log.e("user removed", "${user.toString()}")

                        }
                    }
                )


            }


        }
    }

    @Composable
    fun ListContentUsersSuscribed(userList: MutableLiveData<Materia>) {
        val users by userList.observeAsState()
        val list = users?.listStudent ?: emptyList()
//        val selectedUsers = remember { mutableStateListOf<User>() } // Track selected users

        LazyColumn {
            items(list) { user ->
                ListItemSuscribedUser(user)
            }
        }

        // You can use selectedUsers for your further processing.
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemSuscribedUser(user: User) {
        var isModalVisible by remember { mutableStateOf(false) }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium)
                .clickable { viewModel.currentMateria.value?.id?.let {
                    viewModel.getActivitiesById(
                        it
                    )
                }

//                    view?.findNavController()?.navigate(R.id.action_materiaFragment_to_asignacionFragment)
                    view?.findNavController()?.navigate(MateriaFragmentDirections.actionMateriaFragmentToAsignacionFragment(user?.id ?: 100000))
                           }
            ,
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular user image
                GlideImage(
                    model = user.imageProfile,
                    contentDescription = "foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)  )

//                Image(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "user",
//                    modifier = Modifier
//                        .size(60.dp)
//                        .clip(CircleShape)
//                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = user.firstName.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = user.lastName.toString(),
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(38.dp)
//                        .background(Color.Red, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                }


            }


        }
    }

    @Composable
    fun ListItemAsignacion(item: Actividad) {
        // Replace with your list item implementation
        // You can use Card, ListItem, or any other Composable to represent a single item in the list
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
                Text(text = item.id.toString())
            }

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
    fun MateriaContent(){

        var button1 by remember { mutableStateOf(true) }
        var button2 by remember { mutableStateOf(false) }
        var isModalVisible by remember { mutableStateOf(false) }
        var isModalAsignacionesVisible by remember { mutableStateOf(false) }
        var tituloAsignacion by remember { mutableStateOf(String()) }
        var descripcionAsignacion by remember { mutableStateOf(String()) }

        val selectedUsers = remember { mutableStateListOf<User>() } // Track selected users




        Column {
            viewModel.currentMateria.value?.let { Header(titulo = it.name) }

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(button1){
                    Text(
                        text = "Listado",
                        style = MaterialTheme.typography.body2,
                        fontSize = 30.sp,
                    )
                    IconButton(onClick = { isModalVisible = true }) {
//                    val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }else{
                    Text(
                        text = "Asignaciones",
                        style = MaterialTheme.typography.body2,
                        fontSize = 30.sp,
                    )
                    IconButton(onClick = { isModalAsignacionesVisible = true }) {
//                    val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }


            }


            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        button1 = true
                        button2 = false
                    },
                    modifier = Modifier.weight(1F),

                    ) {
                    Text(text = "Alumnos", style = MaterialTheme.typography.subtitle1)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        button2 = true
                        button1 = false
                    },
                    modifier = Modifier.weight(1F)
                ) {
                    Text(text = "Asignaciones", style = MaterialTheme.typography.subtitle1)
                }

            }

            if (button1) {
                ListContentUsersSuscribed(userList = viewModel.currentMateria)
            } else {
                ListContentAsignacion(listOfActivities = viewModel.activitiesListById)
            }

            if (isModalVisible) {
                Dialog(
                    onDismissRequest = { isModalVisible = false },
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color(245, 245, 245),
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                                .drawWithContent {
                                    drawContent()

//                                    drawShadow(16.dp, shape = RoundedCornerShape(16.dp))
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Agregar alumnos",
                                        style = TextStyle(fontSize = 24.sp)
                                    )
                                    Spacer(modifier = Modifier.weight(1F))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { isModalVisible = false }
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .fillMaxWidth()
                                ) {

                                        ListContentUsers(userList = viewModel.userList, selectedUsers)

                                }
                                Button(
                                    onClick = {
                                        val currentMateria = viewModel.currentMateria.value
                                        if (currentMateria != null) {
                                            val materiaId = currentMateria.id
                                            val teacherId = currentMateria.idTeacher
                                            val materiaName = currentMateria.name

                                            for (user in selectedUsers) {
                                                // Create a modified user object with additional data
                                                val materiaUser = Materia(
                                                    id = currentMateria.id,
                                                    name = currentMateria.name,
                                                    idTeacher = currentMateria.idTeacher
                                                )
                                                val modifiedUser = User(
                                                    id = user.id, // Replace with the actual property name for the user ID
                                                    firstName = user.firstName,
                                                    lastName = user.lastName,
                                                    email = user.email,
                                                    password = user.password,
                                                    gender = user.gender,
                                                    rol = user.rol,
                                                    birthday = user.birthday,
                                                    imageProfile = user.imageProfile,
                                                    phone = user.phone,
                                                    cedula = user.cedula,
                                                    listActivities = user.listActivities,
                                                    lgn = user.lgn,
                                                    lat = user.lat,
                                                    listOfMaterias = user.listOfMaterias?.plus(
                                                        materiaUser
                                                    )
                                                )

                                                // Call viewModel.updateUser for each user in the list
                                                viewModel.updateUser(user?.id ?: 110, modifiedUser)

                                                // Update the materia with the modified user list
                                            }

                                            viewModel.updateMateria(materiaId, Materia(materiaId, teacherId, selectedUsers, materiaName))
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(text = "Actualizar")
                                }
                            }
                        }
                    }
                )
            }

            //Dialog asignaciones
            if (isModalAsignacionesVisible) {

                Dialog(
                    onDismissRequest = { isModalAsignacionesVisible = false },
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Agregar Asignación",
                                        style = TextStyle(fontSize = 24.sp)
                                    )
                                    Spacer(modifier = Modifier.weight(1F))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { isModalAsignacionesVisible = false }
                                    )
                                }

                                    Column {

                                        Text(text = "Titulo")
                                        OutlinedTextField(value = tituloAsignacion,
                                            onValueChange = { tituloAsignacion = it},
                                            placeholder = {
                                                Text(text = "Nombre de la actividad")
                                            }
                                        )

                                        Text(text = "Descripción")
                                        OutlinedTextField(value = descripcionAsignacion,
                                            onValueChange = {descripcionAsignacion = it},
                                            placeholder = {
                                                Text(text = "Describa la actividad")
                                            }
                                        )

                                        Button(onClick = {
                                            val newFragment = DatePickerFragment()
                                            newFragment.show(childFragmentManager, "datePicker")
                                        }) {
                                            Text(text = "Fecha")
                                        }
                                    }


                                Button(
                                    onClick = { viewModel.createActivity(
                                        Actividad(
                                            idClass = viewModel.currentMateria.value!!.id,
                                            title = tituloAsignacion,
                                            description = descripcionAsignacion,
                                            date = viewModel.birthday.value,

                                        )
                                    )},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(text = "Actualizar")
                                }
                            }
                        }
                    }
                )
            }

        }
    }
}