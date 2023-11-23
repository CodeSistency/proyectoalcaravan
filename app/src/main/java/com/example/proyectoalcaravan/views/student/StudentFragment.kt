package com.example.proyectoalcaravan.views.student

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia

import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.generateRandomColor
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.ColorList
import com.example.proyectoalcaravan.views.componentes.shimmer.ShimmerCardList
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StudentFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

//    private var textResult = mutableStateOf("")
//
//
//    private val barCodeLauncher = registerForActivityResult(ScanContract()) {
//            result ->
//        if (result.contents == null) {
////            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
//            Log.e("Scanner error", "ERROR")
//        } else {
//            textResult.value = result.contents
//        }
//    }
//    private fun showCamera(){
//        val options = ScanOptions()
//        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
//        options.setPrompt("Escanea código QR")
//        options.setCameraId(0)
//        options.setBeepEnabled(false)
//        options.setOrientationLocked(false)
//
//        barCodeLauncher.launch(options)
//    }
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) {
//        isGranted ->
//        if(isGranted){
//            showCamera()
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentLifecycle", "Fragment created: ${javaClass.simpleName}")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                StudentContent()

            }
        }
    }

//    private fun checkCamaraPermissions(composeView: ComposeView) {
//
//    }

//    private fun checkCamaraPermissions() {
//        val context = requireContext()
//
//        if (ContextCompat.checkSelfPermission(
//            context,
//            android.Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED) {
//            showCamera()
//        }
//
//        else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
//            Log.e("permission", "Camara requerida")
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
//        }
//    }

    @Composable
    fun MyFragmentContent(loginViewModel: MainViewModel) {
        val currentUser: User? by loginViewModel.currentUser.observeAsState()
        Text(text = "user first name: ${currentUser?.firstName.toString()} user last name: ${currentUser?.lastName.toString()}")
    }

    @Composable
    fun Title(userDB: MutableLiveData<UserDB?>) {
        var user = viewModel.currentUser.observeAsState().value

        var userDatabase = userDB.observeAsState()
        // Replace "Your Title" with your actual title string
        Text(text = "Hola ${user?.firstName ?: userDatabase.value?.firstName}",
            style = MaterialTheme.typography.h1,
            fontSize = 40.sp)
    }





    @Composable
    fun ListItem(item: Materia) {

        var user = viewModel.currentUser.observeAsState()
        var userDB = viewModel.currentUserDB.observeAsState()

        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clickable {

                    if (isClickable) {
                        isClickable = false

                        coroutineScope.launch {
                            delay(2000) // Adjust the delay duration as needed (in milliseconds)
                            isClickable = true
                        }

                        Log.e("Rol", viewModel.currentUser.value?.rol.toString())
                        Log.e("Rol", viewModel.currentUserDB.value?.rol.toString())


                        if (userDB != null) {
                            if (viewModel.currentUser.value?.rol == "Estudiante" || viewModel.currentUserDB.value?.rol == "Estudiante") {

                                view
                                    ?.findNavController()
                                    ?.navigate(
                                        StudentFragmentDirections.actionStudentFragmentToAsignacionFragment(
                                            user?.value?.id ?: 1000
                                        )
                                    )
                            } else {
                                viewModel.getMateriaById(item.id, requireContext())
                                viewModel.currentMateria.postValue(item)
                                view
                                    ?.findNavController()
                                    ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
                            }
                        }


                        viewModel.getActivitiesById(item.id, requireContext())

                    }
//                    viewModel.getMateriaById(item.id)
//                    viewModel.currentMateria.postValue(item)


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
    fun ListContent() {
        val materias = viewModel.currentUser.value?.listOfMaterias ?: emptyList()

        LazyColumn {
            items(materias) { materia ->
                ListItem(item = materia)
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
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
//                        start = Offset(0f, 0f), // Adjust the start and end offsets as needed
//                        end = Offset(100f, 100f)
                    ),
                    shape = CircleShape
                )
                .shadow(8.dp, CircleShape), // Add shadow with a specified elevation
        cutoutShape = CircleShape        ) {
            BottomNavigationItem(
                selected = true,
                onClick = { /* Handle bottom navigation item click */ },
                icon = {
                    Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home))
                }
            )
//            BottomNavigationItem(
//                selected = false,
//                onClick = { /* Handle bottom navigation item click */ },
//                icon = {
//                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Tarea")
//                }
//            )
            BottomNavigationItem(
                selected = false,
                onClick = {
                    view?.findNavController()?.navigate(StudentFragmentDirections.actionStudentFragmentToProfileFragment(user?.id ?: 100000))


                },
                icon = {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Usuario")
                }
            )
        }
    }

    @Composable
    fun Header3(titulo: String, subtitulo: String){
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .wrapContentHeight()
                .background(
//                    colorResource(id = R.color.primary),
                    brush = Brush.verticalGradient(
                        listOf(
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


            }
        }
    }
    @Composable
    fun HorizontalItemCard(item: Materia) {
        var user = viewModel.currentUser.value
        val backgroundColor = ColorList.colors[item.id % ColorList.colors.size]

        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .padding(4.dp)
                .height(90.dp)
                .width(130.dp)
//                .size(150.dp)
                .clip(RoundedCornerShape(16.dp))
//                .background(colorResource(id = R.color.accent))
                .border(1.5.dp, colorResource(id = R.color.blue_dark), RoundedCornerShape(16.dp))
                .clickable {
                    if (isClickable) {
                        isClickable = false

                        // Launch a coroutine to re-enable clickable after a delay
                        coroutineScope.launch {
                            delay(2000) // Adjust the delay duration as needed (in milliseconds)
                            isClickable = true
                        }
                        viewModel.getActivitiesById(item.id, requireContext())
//                    viewModel.getMateriaById(item.id)
//                    viewModel.currentMateria.postValue(item)
                        if (viewModel.currentUser.value?.rol == "Estudiante" || viewModel.currentUserDB.value?.rol == "Estudiante") {

                            view
                                ?.findNavController()
                                ?.navigate(
                                    StudentFragmentDirections.actionStudentFragmentToAsignacionFragment(
                                        user?.id ?: 1000
                                    )
                                )
                        } else {
                            viewModel.getMateriaById(item.id, requireContext())
                            viewModel.currentMateria.postValue(item)
                            view
                                ?.findNavController()
                                ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
                        }
                    }


                },


//            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.accent))
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ){
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Center),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
                Box(modifier = Modifier
                    .background(Color.White)
                    .height(30.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                ){
                    Text(
                        text = "Ir a a materia",
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterStart),
                        color = Color.Gray,

                        fontSize = 13.sp
                    )
                }
            }
            // Display text at the middle left

            Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f), // Adjust alpha for transparency
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .padding(end = 5.dp)
//                    .absolutePadding(top = (-30).dp, right = (-30).dp)
            )
        }


    }


    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun HorizontalList(gridItems: List<Materia>?) {
//        val items by gridItems.observeAsState(initial = emptyList())
        var refresh = viewModel.refreshingCurrentUser.observeAsState()
        val user = viewModel.currentUser.observeAsState()


        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getUserRefresh(viewModel.currentUserDB.value?.userId ?: 10000, requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(modifier = Modifier.pullRefresh(pullRefreshState)){
                LazyRow(
                    modifier = Modifier
//                    .fillMaxWidth()
                        .padding(8.dp)

                ) {
                    items(gridItems ?: user.value?.listOfMaterias ?: emptyList()) { item ->
                        HorizontalItemCard(item = item)
                    }
                }
            }
        }




    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContentAsignacionGeneral(user: MutableLiveData<UserDB?>) {
        val actividades by user.observeAsState()
        Log.e("actividades", actividades.toString())
        Log.e("user test", user.toString())
        var userRol = viewModel.currentUser.value
        Log.e("user test", userRol.toString())
        var refresh = viewModel.refreshingCurrentUser.observeAsState()
        var currentUser = viewModel.currentUser.observeAsState()


        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { user.value?.userId?.let {
            viewModel.getUserRefresh(
                it, requireContext())
        } })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(modifier = Modifier.pullRefresh(pullRefreshState).padding(2.dp)){
                LazyColumn {
                    items(actividades?.listActivities ?: currentUser.value?.listActivities ?: emptyList()) { actividad ->
                        if (actividad != null) {
                            ListItemAsignacionGeneral(item = actividad)
                        }
                    }
                }
                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

            }
        }


    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacionGeneral(item: Actividad) {

        var modalVisible by remember { mutableStateOf(false) }
//        val user by viewModel.updatedUser.observeAsState()
//        var user = viewModel.updatedUser.value
        var currentUser = viewModel.currentUser.value
        var currentUserDB = viewModel.currentUserDB.value

        var mensaje by remember { mutableStateOf(String()) }
        val profileImageUri by viewModel.profileImage.observeAsState()



        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clickable {
                    view
                        ?.findNavController()
                        ?.navigate(
                            StudentFragmentDirections.actionStudentFragmentToAsignacionFragment(
                                currentUserDB?.userId ?: 1000
                            )
                        )
                },
            elevation = 4.dp,

            ) {


            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
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


                if (currentUserDB?.rol == "Profesor"){
                    Text(text = item.calificationRevision.toString())
                }else{
                    IconButton(onClick = {
                        viewModel.getActivitiesById(item.idClass, requireContext())
//                    viewModel.getMateriaById(item.id)
//                    viewModel.currentMateria.postValue(item)
//                        if (viewModel.currentUserDB.value?.rol == "Estudiante") {

                            view
                                ?.findNavController()
                                ?.navigate(
                                    StudentFragmentDirections.actionStudentFragmentToAsignacionFragment(
                                        currentUserDB?.userId ?: 1000
                                    )
                                )
//                        } else {
//                            viewModel.getMateriaById(item.idClass)
//                            view
//                                ?.findNavController()
//                                ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
//                        }

                    }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "algo")
                    }
                }





//



            }



        }


    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun StudentContent() {

        var skipHalfExpanded by remember { mutableStateOf(true) }
        val state = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = skipHalfExpanded
        )
        val scope = rememberCoroutineScope()

        var isModalVisible by remember { mutableStateOf(false) }

        var userDB = viewModel.currentUserDB
        var user = viewModel.currentUser.observeAsState()
        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()


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

                            Title(userDB)

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {

                                if(isClickable){
                                    coroutineScope.launch {
                                        delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                        isClickable = true
                                    }
                                    isModalVisible = true
                                } }) {
//                    IconButton(onClick = { scope.launch { state.show() }}) {
                                val iconPainter: Painter = painterResource(R.drawable.qr_detailed_svgrepo_com)
                                Icon(
                                    painter = iconPainter,
                                    contentDescription = "QR Code",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            IconButton(onClick = {
                                if(isClickable){
                                    coroutineScope.launch {
                                        delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                        isClickable = true
                                    }
                                    view?.findNavController()
                                        ?.navigate(StudentFragmentDirections.actionStudentFragmentToProfileFragment(3000))
                                }
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
                modifier = Modifier.fillMaxSize(),
            ) {
                Header3(titulo = "Aprende", subtitulo ="Virtualmente")

                if(userDB.value?.listOfMaterias?.isNullOrEmpty() == true || user.value?.listOfMaterias?.isNullOrEmpty() == true ){
//                   Box(
//                       modifier = Modifier
//                           .background(
//                               Color.LightGray,
//                               shape = RoundedCornerShape(16.dp)
//                           )
//                           .height(120.dp)
//                           .fillMaxWidth()
//                           .padding(15.dp),
//
//
//
//                   ) {
//                       Column(
//                           modifier = Modifier.fillMaxSize()
//                               ,
//                           verticalArrangement = Arrangement.Center,
//                           horizontalAlignment = Alignment.CenterHorizontally
//                       ) {
//                           Text("No hay materias disponibles")
//                       }
//
//                   }
                    null
                }else{
                    HorizontalList(gridItems = viewModel.currentUserDB.observeAsState().value?.listOfMaterias)
                }
//                HorizontalList(gridItems = viewModel.materiasList)

//                if(userDB.value?.listActivities?.isNotEmpty() == true && user.value?.listActivities?.isNotEmpty() == true){
                if(userDB.value?.listActivities?.isNotEmpty() == true && user.value?.listActivities?.isNotEmpty() == true){

                    Log.e("activities empty or not", userDB.value?.listActivities?.toString() + user.value?.listActivities)
                        //Tengo que hacer progress bar
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                    ) {
                        Text(text = "No hay entregas", fontSize = 30.sp)
                    }
                    }else{
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(text = "Actividades", fontWeight = FontWeight.Bold, fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            ListContentAsignacionGeneral(user = userDB)

                        }

                    }

                if (isModalVisible) {
                    Dialog(
                        onDismissRequest = { isModalVisible = false },
                        content = {
                            Column(
                                modifier = Modifier

                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = 4.dp
                                ) {
                                    Text(text = "Escanea el Qr",
                                        modifier = Modifier.padding(20.dp),
                                        fontSize = 20.sp)

                                        if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.cedula.toString())) {
                                            Barcode(
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)

                                                    .padding(10.dp, 10.dp, 10.dp, 40.dp),
                                                resolutionFactor = 10, // Optionally, increase the resolution of the generated image
                                                type = BarcodeType.QR_CODE, // pick the type of barcode you want to render
                                                value = viewModel.currentUser.value?.cedula.toString() // The textual representation of this code
                                            )
                                        }



                                }



                            }

                        }
                    )
                }

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent ={


                        if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.id.toString())) {
                            Barcode(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxSize()
                                    .padding(10.dp, 10.dp, 10.dp, 40.dp),
                                resolutionFactor = 10, // Optionally, increase the resolution of the generated image
                                type = BarcodeType.QR_CODE, // pick the type of barcode you want to render
                                value = viewModel.currentUser.value?.cedula.toString() // The textual representation of this code
                            )
                        }

                    }) {

                }
            }
        }
    }


}