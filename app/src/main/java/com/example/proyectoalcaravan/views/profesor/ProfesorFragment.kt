package com.example.proyectoalcaravan.views.profesor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.scanner.Scanner

//import kotlinx.coroutines.launch


@ExperimentalGetImage class ProfesorFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    //    val qrCode = viewModel.currentUser.value?.cedula
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
                ProfesorContent()

            }
        }
    }


    @Composable
    fun Title() {
        // Replace "Your Title" with your actual title string
        Text(text = "Home", style = MaterialTheme.typography.h5)
    }


    @Composable
    fun SearchBar() {

        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("todos") }

        var expandedGender by remember { mutableStateOf(false) }
        var selectedOptionGender by remember { mutableStateOf("todos") }

        var isModalVisibleEmail by remember { mutableStateOf(false) }
        var isModalVisibleEdad by remember { mutableStateOf(false) }

        var nombreSearch by remember { mutableStateOf(String()) }
        var emailSearch by remember { mutableStateOf(String()) }
        var edadSearch by remember { mutableStateOf(String()) }







        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1F)
                    .padding(end = 4.dp)
                    .background(Color.White)
                    .padding(12.dp)
                    .clip(MaterialTheme.shapes.medium), // Rounded corners
                value = nombreSearch,
                onValueChange = { nombreSearch = it },
                placeholder = {
                    Text(text = "Buscar", style = MaterialTheme.typography.body1)
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color.Blue, shape = RoundedCornerShape(16.dp))
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            ) {
                IconButton(
                    onClick = {
                              viewModel.getAUserStudentsByFirstName(nombreSearch)
                              },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    Icon(

                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }

            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filters),
                    contentDescription = "Filter",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }

            Box(
                modifier = Modifier
                    .offset(x = 45.dp)
                    .padding(5.dp)
            ){
                DropdownMenu(


                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        viewModel.getUserStudents("Estudiante")
                        selectedOption = "todos"
                        expanded = false
                    }) {
                        Text("Todos")
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "email"
//                        expanded = false
                        isModalVisibleEmail = true
                    }) {
                        Text("Email")
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "edad"
//                            expanded = false
                        isModalVisibleEdad = true

                    }) {
                        Text("Edad")
                    }
                    DropdownMenuItem(onClick = {
                        selectedOption = "genero"
                        expandedGender = true
                    }) {
                        Text("Genero")

                        Box(
                            modifier = Modifier
                                .offset(x = 105.dp)
                                .padding(5.dp)
                        ){
                            DropdownMenu(
                                modifier = Modifier
                                    .wrapContentHeight(),
//                            .align(Alignment.CenterVertically),
                                expanded = expandedGender,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(onClick = {
                                    viewModel.getAUserByGender("Femenino")
                                    selectedOptionGender = "Mujer"
                                    expandedGender = false
                                }) {
                                    Text("Mujer")
                                }
                                DropdownMenuItem(onClick = {
                                    viewModel.getAUserByGender("Masculino")
                                    selectedOptionGender = "Hombre"
                                    expandedGender = false
                                }) {
                                    Text("Hombre")
                                }
                            }
                        }


                    }

                    DropdownMenuItem(onClick = {

                        expanded = false
                    }) {
                        OutlinedButton(onClick = { expanded = false }) {
                            Text(text = "Cerrar")
                        }
                    }
                }
            }



        }

        if (isModalVisibleEdad) {
            Dialog(
                onDismissRequest = { isModalVisibleEdad = false },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(10.dp),

                            elevation = 8.dp

                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    value = edadSearch,
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) {
                                            edadSearch = it
                                        }
                                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    placeholder = {
                                        Text(text = "Buscar...", style = MaterialTheme.typography.body1)
                                    },

                                    singleLine = true,
                                    shape = MaterialTheme.shapes.medium)

                                Button(onClick = {
                                    isModalVisibleEdad = false

                                }) {
                                    Text(text = "Buscar")
                                }
                            }

                        }
                    }
                }
            )
        }

        if (isModalVisibleEmail) {
            Dialog(
                onDismissRequest = { isModalVisibleEmail = false },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(10.dp),
                            elevation = 8.dp

                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    value = emailSearch,
                                    onValueChange = { emailSearch = it },
                                    placeholder = {
                                        Text(text = "Buscar...", style = MaterialTheme.typography.body1)
                                    },
                                    singleLine = true,
                                    shape = MaterialTheme.shapes.medium)
                                Button(onClick = {
                                    viewModel.getUserStudentsByEmail(emailSearch)
                                    isModalVisibleEmail = false
                                    expanded = false
                                }) {
                                    Text(text = "Buscar")
                                }
                            }

                        }
                    }
                }
            )
        }
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItem(item: User) {

        var isModalVisible by remember { mutableStateOf(false) }
        val ctx = LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(Color(245, 245, 245))
                .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium),
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    model = item.imageProfile,
                    contentDescription = "foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // User information
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.firstName.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = item.lastName.toString(),
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { item.phone?.let { viewModel.makeCall(it, ctx) } },
                        modifier = Modifier
                            .size(38.dp)
//                        .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(38.dp)
//                        .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }

                    IconButton(
                        onClick = { isModalVisible = true },
                        modifier = Modifier
                            .size(38.dp)
//                        .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }


            }

            if (isModalVisible) {
                Dialog(
                    onDismissRequest = { isModalVisible = false },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(500.dp)
                                    .height(300.dp),
                                elevation = 8.dp

                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Estas seguro que deseas eliminar este usuario",
                                        style = MaterialTheme.typography.body2,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(10.dp)

                                    )
                                    Button(
                                        onClick = {
                                            item.id?.let { viewModel.deleteUser(it) }
                                            isModalVisible = false
                                        },

                                        ) {
                                        Text(text = "Eliminar")
                                    }
                                    Button(onClick = {
                                        isModalVisible = false
                                    }) {
                                        Text(text = "Cancelar")

                                    }
                                }

                            }
                        }
                    }
                )
            }

        }

    }

    @Composable
    fun ListContent(userList: MutableLiveData<List<User>>) {
        val users by userList.observeAsState(initial = emptyList())

        LazyColumn {
            items(users) { user ->
                ListItem(item = user)
            }
        }
    }

    @Composable
    fun ListContentOrderAlphabet(userList: MutableLiveData<List<User>>) {
        Log.e("alphabet", userList.toString())
        val users by userList.observeAsState(initial = emptyList())
//        val alphabet = users.sortedBy { it.firstName }
//        val alphabet = users.sortedByDescending { it.firstName }
        val alphabet = users.sortedBy { it.firstName }



        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            items(alphabet) { user ->
                ListItem(item = user)
            }

        }
    }

    @Composable
    fun BottomAppBarContent() {
        var user = viewModel.currentUser.value
        BottomAppBar(
            cutoutShape = MaterialTheme.shapes.small
        ) {
            BottomNavigationItem(
                selected = true,
                onClick = { /* Handle bottom navigation item click */ },
                icon = {
                    Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home))
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {
                    view?.findNavController()
                        ?.navigate(R.id.action_profesorFragment_to_clasesFragment)

                },
                icon = {
                    Icon(imageVector = Icons.Default.List, contentDescription = "Actividades")
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {
                    view?.findNavController()
                        ?.navigate(ProfesorFragmentDirections.actionProfesorFragmentToProfileFragment(user?.id ?: 100000))
                },
                icon = {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Usuario")
                }
            )
        }
    }

    @Composable
    fun BottomSheetContent() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(10) { index ->
                Text(text = "Item $index")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ProfesorContent() {
//        var isBottomSheetOpen by remember { mutableStateOf(false) }
        var skipHalfExpanded by remember { mutableStateOf(false) }
        val state = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = skipHalfExpanded
        )
        val scope = rememberCoroutineScope()

        var isModalVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Optional padding
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Title()
                    IconButton(onClick = { isModalVisible = true }) {
//                    IconButton(onClick = { scope.launch { state.show() }}) {
                        val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
                        Icon(
                            painter = iconPainter,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                }

            },
            bottomBar = { BottomAppBarContent() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar()
//                ListContent(userList = viewModel.userStudentsList)

                ListContentOrderAlphabet(userList = viewModel.userStudentsList)

                if (isModalVisible) {
                    Dialog(
                        onDismissRequest = { isModalVisible = false },
                        content = {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
//                                view?.let { it1 -> QrCodeScanner(it1) }
//                                view?.let { it1 -> Scanner(requireView()) }
                                view?.let { it1 -> Scanner(requireView()) }

                            }


//                                Button(onClick = { isModalVisible = false }) {
//                                        Text(text = "Cancelar")
//
//
//
//                            }
                        }
                    )
                }

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent = {


//                        QrCodeScanner()

                    }) {

                }
            }
        }


    }


}