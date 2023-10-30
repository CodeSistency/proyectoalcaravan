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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.Header
import com.example.proyectoalcaravan.views.qrScanner.QrCodeScanner

class MateriaFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.getAllMaterias()
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
    fun ListContentUsers(userList: MutableLiveData<List<User>>) {
        val users = userList.value ?: emptyList()
        val selectedUsers = remember { mutableStateListOf<User>() } // Track selected users

        LazyColumn {
            items(users) { user ->
                ListItemUser(user, selectedUsers)
            }
        }

        // You can use selectedUsers for your further processing.
    }

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
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "user",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

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

//    @Composable
//    fun ListItem (item: Materia) {
//        Card {
//            Text(text = item.name)
//        }
//    }
//
//    @Composable
//    fun ListContent(materiasList: MutableLiveData<List<Materia>>) {
////        val materias by materiasList.observeAsState(initial = emptyList())
//        val materias = materiasList.value ?: emptyList()
//
//        LazyColumn {
//            items(materias) { materia ->
//                ListItem(item = materia)
//            }
//        }
//
////        LaunchedEffect(materiasList) {
////            viewModel.getAllMaterias()
////        }
//    }


     //Diseno de carta para Materia o asignaciones

//@Composable
//fun ListItem(item: String, titulo: String, subtitulo: String) {
//    // Replace with your list item implementation
//    // You can use Card, ListItem, or any other Composable to represent a single item in the list
//    Card(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(4.dp),
//        elevation = 4.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(text = titulo,
//                    style = MaterialTheme.typography.h1,
//                    modifier = Modifier.padding(bottom = 2.dp),
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//
//                    )
//                Text(text = subtitulo,
//                    style = MaterialTheme.typography.h5,
//                    modifier = Modifier.padding(bottom = 2.dp),
//                    fontSize = 15.sp,
//                    color = Color.DarkGray
//                )
//            }
//
//            Text(text = item,
////            modifier = Modifier.padding(16.dp),
//                color = Color.LightGray
//            )
//        }
//
//    }
//}
//
//    @Composable
//    fun ListContent() {
//        // Replace with your list content
//        LazyColumn {
//            items(20) { index ->
//                ListItem(item = "$index", titulo = "Titulo", subtitulo = "Subtitulo")
//            }
//        }
//    }




    @Composable
    fun MateriaContent(){

        var button1 by remember { mutableStateOf(true) }
        var button2 by remember { mutableStateOf(false) }
        var isModalVisible by remember { mutableStateOf(false) }


        Column {
            viewModel.currentMateria.value?.let { Header(titulo = it.name) }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
//                ListContentUsers(userList = viewModel.userList)
            } else {
//                ListContent()
            }

            if (isModalVisible) {
                Dialog(
                    onDismissRequest = { isModalVisible = false },
                    content = {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .background(Color.LightGray,
                                    shape = RoundedCornerShape(16.dp),)
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
                                        text = "Titulo",
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

//                                        ListContentUsers(userList = viewModel.userList)

                                }
                                Button(
                                    onClick = { isModalVisible = false },
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