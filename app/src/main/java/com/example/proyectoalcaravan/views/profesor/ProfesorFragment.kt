package com.example.proyectoalcaravan.views.profesor

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.charts.AgeRangePerformanceChart
import com.example.proyectoalcaravan.views.charts.GenderPerformanceChart
import com.example.proyectoalcaravan.views.componentes.ColorList
import com.example.proyectoalcaravan.views.componentes.shimmer.ShimmerCardList
import com.example.proyectoalcaravan.views.scanner.Scanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import kotlinx.coroutines.launch

const val MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 123

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
                viewModel.getUserStudents("Estudiante", requireContext())
                ProfesorContent()

            }
        }
    }


    @Composable
    fun Title() {
        var userDB = viewModel.currentUserDB.observeAsState()
        var user = viewModel.currentUser.observeAsState().value
        // Replace "Your Title" with your actual title string
        Text(text = "Hola ${user?.firstName ?: userDB?.value?.firstName}",
            style = MaterialTheme.typography.h1,
            fontSize = 30.sp)
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SearchBar(scope: CoroutineScope, state: ModalBottomSheetState) {

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
//                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                , // Rounded corners
                value = nombreSearch,
                onValueChange = { nombreSearch = it
                    viewModel.setSearchQuery(it)
                                },
                placeholder = {
                    Text(text = "Buscar", style = MaterialTheme.typography.body1)
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

//            Box(
//                modifier = Modifier
//                    .size(45.dp)
//                    .background(Color.Blue, shape = RoundedCornerShape(16.dp))
//                    .shadow(8.dp, shape = RoundedCornerShape(16.dp))
//            ) {
//                IconButton(
//                    onClick = {
//                              viewModel.getAUserStudentsByFirstName(nombreSearch)
//                              },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(4.dp)
//                ) {
//                    Icon(
//
//                        painter = painterResource(R.drawable.ic_search),
//                        contentDescription = "Search",
//                        tint = Color.White
//                    )
//                }
//            }

            IconButton(
                onClick = { expanded = true
                    scope.launch { state.show() }},
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filters),
                    contentDescription = "Filter",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }

//            Box(
//                modifier = Modifier
//                    .offset(x = 45.dp)
//                    .padding(5.dp)
//            ){
//                DropdownMenu(
//
//
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    DropdownMenuItem(onClick = {
//                        viewModel.getUserStudents("Estudiante", requireContext())
//                        selectedOption = "todos"
//                        expanded = false
//                    }) {
//                        Text("Todos")
//                    }
//                    DropdownMenuItem(onClick = {
//                        selectedOption = "email"
////                        expanded = false
//                        isModalVisibleEmail = true
//                    }) {
//                        Text("Email")
//                    }
//                    DropdownMenuItem(onClick = {
//                        selectedOption = "edad"
////                            expanded = false
//                        isModalVisibleEdad = true
//
//                    }) {
//                        Text("Edad")
//                    }
//                    DropdownMenuItem(onClick = {
//                        selectedOption = "genero"
//                        expandedGender = true
//                    }) {
//                        Text("Genero")
//
//                        Box(
//                            modifier = Modifier
//                                .offset(x = 105.dp)
//                                .padding(5.dp)
//                        ){
//                            DropdownMenu(
//                                modifier = Modifier
//                                    .wrapContentHeight(),
////                            .align(Alignment.CenterVertically),
//                                expanded = expandedGender,
//                                onDismissRequest = { expanded = false }
//                            ) {
//                                DropdownMenuItem(onClick = {
//                                    viewModel.getAUserByGender("Femenino", requireContext())
//                                    selectedOptionGender = "Mujer"
//                                    expandedGender = false
//                                }) {
//                                    Text("Mujer")
//                                }
//                                DropdownMenuItem(onClick = {
//                                    viewModel.getAUserByGender("Masculino", requireContext())
//                                    selectedOptionGender = "Hombre"
//                                    expandedGender = false
//                                }) {
//                                    Text("Hombre")
//                                }
//                            }
//                        }
//
//
//                    }
//
//                    DropdownMenuItem(onClick = {
//
//                        expanded = false
//                    }) {
//                        OutlinedButton(onClick = { expanded = false }) {
//                            Text(text = "Cerrar")
//                        }
//                    }
//                }
//            }



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
                                    viewModel.getUserStudentsByEmail(emailSearch, requireContext())
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

    @Composable
    fun FilterIconButton(icon: ImageVector, text: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle button click */ }) {
                Icon(imageVector = icon, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text)
        }
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItem(item: User, isPermissionGranted: Boolean) {

        var isModalVisible by remember { mutableStateOf(false) }
        var isModalContactVisible by remember { mutableStateOf(false) }

        val ctx = LocalContext.current


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .clickable {
                    view
                        ?.findNavController()
                        ?.navigate(
                            ProfesorFragmentDirections.actionProfesorFragmentToProfileFragment(
                                item?.id ?: 3000
                            )
                        )
                },

            elevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    model = item.imageProfile,
                    contentDescription = "foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit

                )

                Spacer(modifier = Modifier.width(16.dp))

                // User information
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${item.firstName} ${item.lastName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = item.email.toString(),
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
                        onClick = {isModalContactVisible = true },
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

//            if (isModalVisible) {
//                Dialog(
//
//                    onDismissRequest = { isModalVisible = false },
//                    content = {
//                        Column(
//                            modifier = Modifier
//                                .background(Color.White)
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                                .shadow(elevation = 10.dp, shape = RectangleShape),
//
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//
//                                Column(
//                                    horizontalAlignment = Alignment.CenterHorizontally
//                                ) {
//                                    Text(
//                                        text = "Estas seguro que deseas eliminar este usuario",
//                                        style = MaterialTheme.typography.subtitle1,
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = 25.sp,
//                                        modifier = Modifier
//                                            .align(Alignment.CenterHorizontally)
//                                            .padding(10.dp)
//
//                                    )
//                                    Button(
//                                        onClick = {
//                                            item.id?.let { viewModel.deleteUser(it, requireContext()) }
//                                            isModalVisible = false
//                                        },
//
//                                        ) {
//                                        Text(text = "Eliminar")
//                                    }
//                                    Button(onClick = {
//                                        isModalVisible = false
//                                    }) {
//                                        Text(text = "Cancelar")
//
//                                    }
//                                }
//
//
//                        }
//                    }
//                )
//            }

            if (isModalVisible) {
                Dialog(
                    onDismissRequest = { isModalVisible = false },
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(elevation = 10.dp, shape = RectangleShape)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "¿Estás seguro que quiere eliminar?",
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontSize = 20.sp
                                )

                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { isModalVisible = false }) {
                                        Text(text = "Cancelar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TextButton(onClick = {
                                        item.id?.let { viewModel.deleteUser(it, requireContext()) }
                                        isModalVisible = false
                                    }) {
                                        Text(text = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                )
            }
            if(isModalContactVisible){
                Dialog(
                    onDismissRequest = { isModalContactVisible = false },
                    content = {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 10.dp, shape = RectangleShape)){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(16.dp)
                                ,
                            ) {
                                Text(
                                    text = "¿Estás seguro que quiere agregar este contacto?",
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Nombre: ${item.firstName.toString()}",
                                    fontSize = 17.sp,
                                    color = Color.Gray

                                )
                                Text(
                                    text = "Número: ${item.phone.toString()}",
                                    fontSize = 17.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { isModalContactVisible = false }) {
                                        Text(text = "Cancelar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    TextButton(onClick = {
                                        item.phone?.let {
                                            if (isPermissionGranted || viewModel.checkContactPermission(requireActivity())) {
                                                // Permission already granted, proceed with the contact operation
                                                viewModel.insertContact(requireActivity(), item.firstName?: "", item.phone.toString())                            } else {
                                                // Permission not granted, request it
                                                ActivityCompat.requestPermissions(
                                                    context as ComponentActivity,
                                                    arrayOf(Manifest.permission.WRITE_CONTACTS),
                                                    MY_PERMISSIONS_REQUEST_WRITE_CONTACTS
                                                )
                                            }
                                        }
                                    }) {
                                        Text(text = "Añadir")
                                    }
                                }
                            }
                        }

                    }
                )
            }

//            if (isModalContactVisible) {
//                Dialog(
//                    onDismissRequest = { isModalContactVisible = false },
//                    content = {
//                        Column(
//                            modifier = Modifier
//                                .background(Color.White)
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                                .shadow(elevation = 10.dp, shape = RectangleShape),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Text(
//                                    text = "Estas seguro que desea agregar añadir este contacto",
//                                    style = MaterialTheme.typography.subtitle1,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 25.sp,
//                                    modifier = Modifier
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(10.dp)
//
//                                )
//
//                                Text(
//                                    text = "Nombre: ${item.firstName} ${item.lastName}",
//                                    style = MaterialTheme.typography.h6,
//                                    fontSize = 18.sp,
//                                    modifier = Modifier
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(5.dp)
//
//                                )
//                                Text(
//                                    text = "Número: 0${item.phone}",
//                                    style = MaterialTheme.typography.h6,
//                                    fontSize = 18.sp,
//                                    modifier = Modifier
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(5.dp)
//
//                                )
//                                Button(
//                                    onClick = {
//                                        item.phone?.let {
//                                            if (isPermissionGranted || viewModel.checkContactPermission(requireActivity())) {
//                                                // Permission already granted, proceed with the contact operation
//                                                viewModel.insertContact(requireActivity(), item.firstName?: "", item.phone.toString())                            } else {
//                                                // Permission not granted, request it
//                                                ActivityCompat.requestPermissions(
//                                                    context as ComponentActivity,
//                                                    arrayOf(Manifest.permission.WRITE_CONTACTS),
//                                                    MY_PERMISSIONS_REQUEST_WRITE_CONTACTS
//                                                )
//                                            }
//                                        }
//                                        isModalContactVisible = false
//                                    },
//
//                                    ) {
//                                    Text(text = "Eliminar")
//                                }
//                                Button(onClick = {
//                                    isModalContactVisible = false
//                                }) {
//                                    Text(text = "Cancelar")
//
//                                }
//                            }
//
//
//                        }
//                    }
//                )
//            }

        }

    }



    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContent(userList: MutableLiveData<List<User>>) {
//        Log.e("user profesor fragment", userList.value.toString())
//        val users by viewModel.userStudentsList.observeAsState(initial = emptyList())
//        Log.e("actividades", users.toString())
        val users by userList.observeAsState(initial = emptyList())
        val userStudents = viewModel.userStudentsList.observeAsState()

        LaunchedEffect(key1 = true){
            viewModel.setSearchQuery("")
        }

        var isPermissionGranted by remember { mutableStateOf(false) }
        var refresh = viewModel.refreshing.observeAsState()

        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getUserStudents("Estudiante", requireContext())
        viewModel.resetFilters(requireContext())})

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            if (users.isNullOrEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No hay resultados")
                }

            }else{
                Box(Modifier.pullRefresh(pullRefreshState)){
                    LazyColumn {
                        if(users != null){
                            items(users) { user ->
                                Log.e("user specify", user.toString())

                                ListItem(item = user, isPermissionGranted)
                            }
                        }


                    }
                    PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))
                }
            }
        }

    }

    @Composable
    fun ListContentOrderAlphabet(userList: List<User>) {
        var isPermissionGranted by remember { mutableStateOf(false) }

        Log.e("alphabet", userList.toString())
//        val users by viewModel.userStudentsList.observeAsState(initial = emptyList())
        val users by viewModel.userStudentsList.observeAsState(initial = emptyList())

        Log.e("users viewModel", viewModel.userStudentsList.value.toString())

        Log.e("actividadesss", users.toString())

//        val alphabet = users.sortedBy { it.firstName }
//        val alphabet = users.sortedByDescending { it.firstName }
        val alphabet = users.sortedBy { it.firstName }



        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            items(alphabet) { user ->
                Log.e("user alphabet", "${user.toString()} ")
                ListItem(item = user, isPermissionGranted)
            }

        }
    }

    @Composable
    fun BottomAppBarContent() {
        var user = viewModel.currentUser.value

        val gradientColors = listOf(
            colorResource(id = R.color.primary),
            colorResource(id = R.color.secondary)
        )

        BottomAppBar(
            modifier = Modifier
                .padding(16.dp) // Add padding to separate from the screen

                .shadow(8.dp, CircleShape), // Add shadow with a specified elevation
            cutoutShape = CircleShape,
            backgroundColor = colorResource(id = R.color.accent)// You can use CircleShape or RoundedCornerShape as per your preference
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
//            BottomNavigationItem(
//                selected = false,
//                onClick = {
//                    view?.findNavController()
//                        ?.navigate(ProfesorFragmentDirections.actionProfesorFragmentToProfileFragment(user?.id ?: 100000))
//                },
//                icon = {
//                    Icon(Icons.Default.AccountCircle, contentDescription = "Usuario")
//                }
//            )
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

//    @Composable
//    fun getCustomGradient(startColor: Color, endColor: Color, percentage: Float): Brush {
//        val context = LocalDensity.current
//        val density = context.density
//        val startY = 0f
//        val endY = 100f
//
//        val shader = LinearGradientShader(
//            startX = 0f,
//            startY = startY,
//            endX = 0f,
//            endY = endY,
//            colors = intArrayOf(startColor.toArgb(), endColor.toArgb()),
//            colorStops = floatArrayOf(0f, percentage)
//        )
//
//        return Brush(shader = shader)
//    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Header3(titulo: String, subtitulo: String, scope: CoroutineScope, state: ModalBottomSheetState){
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .wrapContentHeight()
                .background(
//                    Color.Blue.copy(alpha = 0.8F),
                    brush = Brush.verticalGradient(
                        listOf(
//                            colorResource(id = R.color.secondary),
//                            colorResource(id = R.color.primary)
                            colorResource(id = R.color.accent),
                            colorResource(id = R.color.blue_dark)
                        )
                    ),
                    shape = RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp),

                    )
                .padding(bottom = 10.dp)
        ){
            Column(
                modifier = Modifier
//                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.Center
            ) {
                Text(text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    color = Color.White,
//                    modifier = Modifier.padding(top = 5.dp)


                )

                Text(text = subtitulo,
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 50.sp,
//                    fontWeight = FontWeight.Bold,
                    color = Color.White,
//                    modifier = Modifier.padding(top = 2.dp)


                )

                SearchBar(scope, state)

            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun TabsBottomSheetWithPagerScreen(state: ModalBottomSheetState, scope: CoroutineScope) {
        var selectedTabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Ordenar", "Email", "Sexo")

        // Pager state
        val pagerState = rememberPagerState(pageCount = {tabs.size})
        val coroutineScope = rememberCoroutineScope()

        // Observe the current page index and update the selectedTabIndex accordingly
        LaunchedEffect(pagerState.currentPage) {
            selectedTabIndex = pagerState.currentPage
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEachIndexed { index, text ->
                    TabItem(
                        text = text,
                        isSelected = index == selectedTabIndex,
                        onTabClick = {
                            selectedTabIndex = index
                            // Set the selected page in the pager state
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Animated Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { page ->
                // Content for each tab
                when (page) {
                    0 -> TabOrdernar()
                    1 -> TabEmail()
                    2 -> TabSexo(state, scope)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    @Composable
    fun TabItem(text: String, isSelected: Boolean, onTabClick: () -> Unit) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clip(MaterialTheme.shapes.small)
                .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent)
                .clickable { onTabClick() }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(8.dp)
                    .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent),
                color = if (isSelected) Color.White else Color.Gray
            )
        }
    }


//    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
//    @Composable
//    fun TabsBottomSheetWithPagerScreen(state: ModalBottomSheetState, scope: CoroutineScope, ) {
//        var selectedTabIndex by remember { mutableStateOf(0) }
//
//        val tabs = listOf("Ordenar", "Email", "Sexo")
//
//
//        // Pager state
//        val pagerState = rememberPagerState(pageCount = {tabs.size})
//        val coroutineScope = rememberCoroutineScope()
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(2.dp)
//        ) {
//            // Tabs
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
////                    .background(Color.LightGray),
////                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                tabs.forEachIndexed { index, text ->
//                    TabItem(
//                        text = text,
//                        isSelected = index == selectedTabIndex,
//                        onTabClick = {
//                            selectedTabIndex = index
//                            // Set the selected page in the pager state
//                            coroutineScope.launch {
//                                // Call scroll to on pagerState
//                                pagerState.animateScrollToPage(index)                            }
//
//                        }
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            // Animated Content
//            HorizontalPager(
//                state = pagerState,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .weight(1f)
//            ) { page ->
//                // Content for each tab
//                when (page) {
//                    0 -> TabOrdernar()
//                    1 -> TabEmail()
//                    2 -> TabSexo(state, scope)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//    }
//
//    @Composable
//    fun TabItem(text: String, isSelected: Boolean, onTabClick: () -> Unit) {
//        Box(
//            modifier = Modifier
//                .padding(4.dp)
//                .clip(MaterialTheme.shapes.small)
//                .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent)
//                .clickable { onTabClick() }
//        ) {
//            Text(
//                text = text,
//                modifier = Modifier
//                    .padding(8.dp)
//                    .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent),
//                color = if (isSelected) Color.White else Color.Gray
//            )
//        }
//    }

    @Composable
    fun TabContent(content: String) {
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)
                .padding(16.dp)
        ) {
            Text(text = content)
        }
    }

    @Composable
    fun TabEmail() {

        var emailSearch by remember { mutableStateOf(String()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)
//                .padding(2.dp)
        ) {
            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Filtrar por email:")
                OutlinedTextField(
                    modifier = Modifier
//                        .weight(1F)
//                    .padding(12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                    , // Rounded corners
                    value = emailSearch,
                    onValueChange = {emailSearch = it
                        viewModel.setEmailFilter(it)

                    },
                    placeholder = {
                        Text(text = "Buscar", style = MaterialTheme.typography.body1)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(onClick = { emailSearch = ""}) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = "Aplicar")
//
//            }
        }
    }

    @Composable
    fun TabEdad() {

        var edadSearch by remember { mutableStateOf(String()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)
//                .padding(2.dp)
        ) {
            Column(
//                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Filtrar por email:")
                OutlinedTextField(
                    modifier = Modifier
//                        .weight(1F)
//                    .padding(12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                    , // Rounded corners
                    value = edadSearch,
                    onValueChange = { edadSearch = it
                        viewModel
                    },
                    placeholder = {
                        Text(text = "Buscar", style = MaterialTheme.typography.body1)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
            }
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = "Aplicar")
//
//            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TabSexo(state: ModalBottomSheetState, scope: CoroutineScope) {


        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Blue)
//                .padding(2.dp)
        ) {
            Column(

                verticalArrangement = Arrangement.Center
            ) {
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceAround,
                 verticalAlignment = Alignment.CenterVertically
             ) {

                 IconButton(
                     onClick = { viewModel.setGenderFilter("Masculino")  },
                     modifier = Modifier
                         .size(100.dp)
                         .background(Color.LightGray, CircleShape)
                 ) {
                     Icon(
                         painterResource(id = R.drawable.ic_male),
                         contentDescription = null,
                     )
                 }


                 IconButton(
                     onClick = { viewModel.setGenderFilter("Femenino")  },
                     modifier = Modifier
                         .size(100.dp)
                         .background(Color.LightGray, CircleShape)
                 ) {
                     Icon(
                         painterResource(id = R.drawable.ic_female),
                         contentDescription = null,

                     )
                 }
             }
            }
//            Button(onClick = { /*TODO*/ }) {
//                Text(text = "Aplicar")
//
//            }
        }
    }

    @Composable
    fun TabOrdernar() {
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .padding(16.dp)
        ) {
            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Ordenar por:")
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    Modifier.padding(horizontal = 10.dp, vertical = 10.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { viewModel.sortByAlphabetical(true) },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.LightGray, CircleShape)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_alphabet),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Alfabeticamente")
                }

                Row(
                    Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { viewModel.sortByBirthday(true) },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.LightGray, CircleShape)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_date),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Fecha de nacimiento")
                }

                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { viewModel.sortByCreationDate() },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.LightGray, CircleShape)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_sort),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = "Fecha de creacion")
                }


//                Row(
//                    modifier = Modifier.padding(3.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    IconButton(
//                        onClick = { viewModel.sortByAge(true) },
//                        modifier = Modifier
//                            .size(38.dp)
//                            .background(Color.LightGray, CircleShape)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = null,
//                            tint = Color.Gray
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(10.dp))
//
//                    Text(text = "Ordenar por edad")
//                }

                Row(
                    modifier = Modifier.padding(10.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Edad")
                    Spacer(modifier = Modifier.width(10.dp))
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.SpaceAround
                   ) {
                       OutlinedButton(onClick = { viewModel.setAgeFilter(17, 26) }) {
                           Text(text = "17-26")
                       }
                       OutlinedButton(onClick = { viewModel.setAgeFilter(27, 36) }) {
                           Text(text = "27-36")
                       }
                       OutlinedButton(onClick = { viewModel.setAgeFilter(47, 56) }) {
                           Text(text = "47-56")
                       }
                   }
                }
            }
        }
    }

    @Composable
    fun GridItemCard(item: Materia) {
        val backgroundColor = ColorList.colors[item.id % ColorList.colors.size]

        Box(
            modifier = Modifier
                .padding(4.dp)
                .height(110.dp)
//                .size(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.accent))
                .border(0.5.dp, colorResource(id = R.color.blue_dark), RoundedCornerShape(16.dp))
                .shadow(4.dp, RoundedCornerShape(16.dp))
//                .background(generateRandomColor())
//                .background(
//                    brush = Brush.linearGradient(
//                        colors = listOf(Color.Black, Color.Transparent),
//                        start = Offset(0.5f, 1.0f),
//                        end = Offset(0.5f, 0.5f)
//                    )
//                )
                .clickable {
                    viewModel.currentMateria.postValue(item)
                    viewModel.getActivitiesById(item.id, requireContext())
                    view
                        ?.findNavController()
                        ?.navigate(R.id.action_profesorFragment_to_materiaFragment)
                },
//            contentAlignment = Alignment.Center
        ) {
            // Display text at the middle left
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterStart),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f), // Adjust alpha for transparency
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .padding(end = 5.dp)
//                    .absolutePadding(top = (-30).dp, right = (-30).dp)
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Grid(gridItems: MutableLiveData<List<Materia>>) {
        val items by gridItems.observeAsState(initial = emptyList())
//        LazyColumn {
//            items(items = items.chunked(2)) { rowItems ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                ) {
//                    for (item in rowItems) {
//                        GridItemCard(item = item)
//                    }
//                }
//            }
//        }
        var refresh = viewModel.refreshingMaterias.observeAsState()


        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getAllMaterias(requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(Modifier.pullRefresh(pullRefreshState)){
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(items) { item ->
                        GridItemCard(item)
                    }
                }
                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

            }
        }


    }



    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabsHomeWithPagerScreen() {
        var selectedTabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Listado", "Materias", "Rendimiento", "Metricas")


        // Pager state
        val pagerState = rememberPagerState(pageCount = {tabs.size})
        val coroutineScope = rememberCoroutineScope()
        val filteredUserList = viewModel.filteredUserList


        // Observe the current page index and update the selectedTabIndex accordingly
        LaunchedEffect(pagerState.currentPage) {
            selectedTabIndex = pagerState.currentPage
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            Arrangement.Top
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .background(color = Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEachIndexed { index, text ->
                    TabItemHome(
                        text = text,
                        isSelected = index == selectedTabIndex,
                        onTabClick = {
                            selectedTabIndex = index
                            // Set the selected page in the pager state
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(index)                            }

                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                // Content for each tab
                when (page) {
                    0 -> Column(
                        modifier = Modifier.padding(bottom = 20.dp, start = 5.dp, end = 5.dp),
                                verticalArrangement = Arrangement.Top) {
                        ListContent(userList = filteredUserList )
                    }
                    1 ->Column(modifier = Modifier.padding(bottom = 30.dp)){
                        Grid(gridItems = viewModel.materiasList)
                    }
                    2 -> Column(modifier = Modifier.padding(bottom = 30.dp, start = 10.dp, end = 10.dp)){
                        GenderPerformanceChart(viewModel = viewModel)
                    }
                    3 ->Column(modifier = Modifier.padding( start = 10.dp, end = 10.dp)){
                        AgeRangePerformanceChart(viewModel = viewModel, context = requireContext())
                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    @Composable
    fun TabItemHome(text: String, isSelected: Boolean, onTabClick: () -> Unit) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clip(MaterialTheme.shapes.small)
                .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent)
                .clickable { onTabClick() }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(8.dp)
                    .background(if (isSelected) colorResource(id = R.color.blue_dark) else Color.Transparent),
                color = if (isSelected) Color.White else Color.Gray
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ProfesorContent() {
        var skipHalfExpanded by remember { mutableStateOf(false) }
        val state = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = skipHalfExpanded
        )
        val scope = rememberCoroutineScope()
        var isClickable by remember { mutableStateOf(true) }


        var isModalVisible by remember { mutableStateOf(false) }
        val filteredUserList = viewModel.filteredUserList

//        LaunchedEffect(key1 = true ){
//            viewModel.setSearchQuery("")
//        }
//
//        LaunchedEffect(key1 = true ){
//            viewModel.getUserStudents("Estudiante")
//        }
        Log.d("UserViewModel", "userStudentsList: ${viewModel.userStudentsList.value}")
        Log.d("UserViewModel", "filteredUserList: ${viewModel.filteredUserList.value}")

        var user = viewModel.currentUser.value
        var userDB = viewModel.currentUserDB.observeAsState()



        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.accent)),                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user != null || userDB != null) {
                                Title()
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                if(isClickable){
                                    scope.launch {
                                        delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                        isClickable = true
                                    }
                                    isModalVisible = true
                                }
                            }) {
//                    IconButton(onClick = { scope.launch { state.show() }}) {
                                val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
                                Icon(
                                    painter = iconPainter,
                                    contentDescription = "QR Code",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            IconButton(onClick = {
                                if(isClickable){
                                    scope.launch {
                                        delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                        isClickable = true
                                    }
                                    view?.findNavController()
                                        ?.navigate(ProfesorFragmentDirections.actionProfesorFragmentToProfileFragment(user?.id ?: 3000))                                 }
                                }) {
//                    IconButton(onClick = { scope.launch { state.show() }}) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "QR Code",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }


                    }

                }

            },
//            bottomBar = { BottomAppBarContent() }
        ) {
            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header3(titulo = "Administra", subtitulo ="tus clases", scope, state )
//                ListContent(userList = viewModel.userStudentsList)

//                    ListContent(userList = filteredUserList)

                    TabsHomeWithPagerScreen()




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
                                view?.let { it1 -> Scanner(requireView(), viewModel, requireContext()) }

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


            }
        }



        ModalBottomSheetLayout(sheetState = state,
            modifier = Modifier.background(Color.Transparent),

            sheetContent = {


                Column(
                    modifier = Modifier

                        .padding(top = 16.dp, end = 16.dp, start = 16.dp, bottom = 80.dp,)


                ) {
                    // Text and icon in the same row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Filtrar por:")
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
//                            OutlinedButton(onClick = { viewModel.resetFilters(requireContext())}) {
//                                Text(text = "Reset")
//                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(painter = painterResource(R.drawable.ic_filters), contentDescription = null)

                        }
                    }
                    TabsBottomSheetWithPagerScreen(state, scope)

                    // Range input
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Slider(
//                        value = 15f,
//                        onValueChange = {},
//                        valueRange = 15f..40f,
//                        steps = 25,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    // Icon buttons
//                    Spacer(modifier = Modifier.height(16.dp))
//                    FilterIconButton(icon = Icons.Default.Person, text = "Age")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    FilterIconButton(icon = Icons.Default.Build, text = "Filter 2")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    FilterIconButton(icon = Icons.Default.Place, text = "Filter 3")
//
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Button(onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 10.dp)) {
//                        Text(text = "Aplicar")
//                    }

                }

            }) {
            if (state.isVisible) {
                // Listen for clicks outside the bottom sheet
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            scope.launch {
                                state.hide()
                            }
                        }
                )
            }
        }
    }


}