package com.example.proyectoalcaravan.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.example.proyectoalcaravan.views.charts.AgeRangePerformanceChart
import com.example.proyectoalcaravan.views.charts.AgeRangePerformanceChartByMateria
import com.example.proyectoalcaravan.views.charts.GenderPerformanceChartByMateria
import com.example.proyectoalcaravan.views.componentes.Header
import com.example.proyectoalcaravan.views.componentes.shimmer.ShimmerCardList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class MateriaFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

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
            viewModel.getAllMaterias(requireContext())
            viewModel.getAllUsers(requireContext())
            setContent {
                MateriaContent()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.activitiesListById.postValue(arrayListOf())
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContentUsers(
        userList: MutableLiveData<List<User>>,
        selectedUsers: MutableList<User>,
        userListSuscribed: MutableLiveData<Materia>
    ) {
        val users by userList.observeAsState(initial = emptyList())
        val usersSuscribed by userListSuscribed.observeAsState()
        val list = usersSuscribed?.listStudent ?: emptyList()
        Log.e("all selected users", selectedUsers.toString())
        // Get the set of user IDs already in the class
        val usersInClassIds = list.map { it.id }.toSet()

        var refresh = viewModel.refreshingMateriasById.observeAsState()
        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getUserStudents("Estudiante", requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(Modifier.pullRefresh(pullRefreshState)){
                LazyColumn {
                    items(users.filter { user -> user.id !in usersInClassIds }) { user ->
                        ListItemUser(user, selectedUsers)
                    }
                }
            }
        }


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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContentUsersSuscribed(userList: MutableLiveData<Materia>) {
        val users by userList.observeAsState()
        val list = users?.listStudent ?: emptyList()
//        val selectedUsers = remember { mutableStateListOf<User>() } // Track selected users
        var refresh = viewModel.refreshingMateriasById.observeAsState()


        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getAllMaterias(requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(Modifier.pullRefresh(pullRefreshState),){
                LazyColumn {
                    items(users?.listStudent ?: emptyList()) { user ->
                        ListItemSuscribedUser(user)
                    }
                }
            }
        }


        // You can use selectedUsers for your further processing.
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemSuscribedUser(user: User) {
        var isModalVisible by remember { mutableStateOf(false) }
        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()
        var isButtonEnabled by remember { mutableStateOf(true) }
        var isProgressModalLoading by remember {
            mutableStateOf(false)
        }



        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium)
                .clickable {
                    if (isClickable) {
                        isClickable = false

                        // Launch a coroutine to re-enable clickable after a delay
                        coroutineScope.launch {
                            delay(2000) // Adjust the delay duration as needed (in milliseconds)
                            isClickable = true
                        }

                        viewModel.currentMateria.value?.id?.let {
                            viewModel.getActivitiesById(
                                it,
                                requireContext()
                            )
                        }

                        user.id?.let { viewModel.getUserById(it, requireContext()) }

//                    view?.findNavController()?.navigate(R.id.action_materiaFragment_to_asignacionFragment)
                        view
                            ?.findNavController()
                            ?.navigate(
                                MateriaFragmentDirections.actionMateriaFragmentToAsignacionFragment(
                                    user?.id ?: 100000
                                )
                            )
                    }

                }
            ,
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Ir a Actividades",
                            fontSize = 14.sp,
                            color = Color.Gray)
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray)

                        IconButton(
                            onClick = {isModalVisible = true},
                            modifier = Modifier
                                .size(38.dp)
//                        .background(Color.Red, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }

            }

        }

        if (isProgressModalLoading) {
            Dialog(
                onDismissRequest = { isProgressModalLoading = false },
                content = {

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = colorResource(id = R.color.blue_dark),


                            )
                    }
                }
            )
        }

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
                                TextButton(
                                    enabled = isButtonEnabled,
                                    onClick = {
                                    if(isButtonEnabled){
                                        isButtonEnabled = false
                                        isProgressModalLoading = true


                                        val currentMateria = viewModel.currentMateria.value
                                        if (currentMateria != null) {
                                            val materiaId = currentMateria.id
                                            val teacherId = currentMateria.idTeacher
                                            val materiaName = currentMateria.name

                                            // Filter out the selected user
                                            val filteredUsers = currentMateria.listStudent?.filterNot { it.id == user.id }
                                            Log.e("remove user", filteredUsers.toString())

                                            // Update the Materia object with the filtered users
                                            val materiaUser = Materia(
                                                id = materiaId,
                                                name = materiaName,
                                                idTeacher = teacherId,
                                                listStudent = filteredUsers
                                            )

                                            val materiaIdToRemove = user.listOfMaterias?.find { it.id == materiaId }?.id ?: 0
                                            val updatedListOfMaterias = user.listOfMaterias?.filterNot { it.id == materiaIdToRemove }

                                            val modifiedUser = User(
                                                id = user.id, // Replace with the actual property name for the user ID
                                                firstName = user.firstName,
                                                lastName = user.lastName,
                                                email = user.email,
                                                password = user.password,
                                                gender = user.gender,
                                                edad = user.edad,
                                                rol = user.rol,
                                                birthday = user.birthday,
                                                imageProfile = user.imageProfile,
                                                phone = user.phone,
                                                cedula = user.cedula,
                                                listActivities = user.listActivities,
                                                lgn = user.lgn,
                                                lat = user.lat,
                                                listOfMaterias = updatedListOfMaterias,
                                                created = user.created
                                            )
                                            viewModel.updateUser(user?.id ?: 110, modifiedUser, requireContext()).observe(viewLifecycleOwner){
                                                if (it){
                                                    isProgressModalLoading = false
                                                    isButtonEnabled = true
                                                }else{
                                                    isProgressModalLoading = false
                                                    isButtonEnabled = true

                                                }
                                            }

                                            isButtonEnabled = true
                                            isProgressModalLoading = false



                                            viewModel.updateMateria(materiaId, materiaUser, requireContext())
                                            viewModel.getMateriaById(materiaId, requireContext())
                                        }
                                        isModalVisible = false
                                        isProgressModalLoading = false

                                    }

                                }) {
                                    Text(text = "Eliminar")
                                }
                            }
                        }
                    }
                }
            )
        }

//        if (isModalVisible) {
//            Dialog(
//                onDismissRequest = { isModalVisible = false },
//                content = {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Card(
//                            modifier = Modifier
//                                .background(Color.White)
//                                .width(500.dp)
//                                .height(300.dp),
//                            elevation = 8.dp
//
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Text(
//                                    text = "Estas seguro que deseas eliminar este usuario",
//                                    style = MaterialTheme.typography.body2,
//                                    fontSize = 15.sp,
//                                    modifier = Modifier
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(10.dp)
//
//                                )
//                                Button(
//                                    onClick = {
//                                        val currentMateria = viewModel.currentMateria.value
//                                        if (currentMateria != null) {
//                                            val materiaId = currentMateria.id
//                                            val teacherId = currentMateria.idTeacher
//                                            val materiaName = currentMateria.name
//
//                                            // Filter out the selected user
//                                            val filteredUsers = currentMateria.listStudent?.filterNot { it.id == user.id }
//                                            Log.e("remove user", filteredUsers.toString())
//
//                                            // Update the Materia object with the filtered users
//                                            val materiaUser = Materia(
//                                                id = materiaId,
//                                                name = materiaName,
//                                                idTeacher = teacherId,
//                                                listStudent = filteredUsers
//                                            )
//
//                                            val materiaIdToRemove = user.listOfMaterias?.find { it.id == materiaId }?.id ?: 0
//                                            val updatedListOfMaterias = user.listOfMaterias?.filterNot { it.id == materiaIdToRemove }
//
//                                            val modifiedUser = User(
//                                                id = user.id, // Replace with the actual property name for the user ID
//                                                firstName = user.firstName,
//                                                lastName = user.lastName,
//                                                email = user.email,
//                                                password = user.password,
//                                                gender = user.gender,
//                                                rol = user.rol,
//                                                birthday = user.birthday,
//                                                imageProfile = user.imageProfile,
//                                                phone = user.phone,
//                                                cedula = user.cedula,
//                                                listActivities = user.listActivities,
//                                                lgn = user.lgn,
//                                                lat = user.lat,
//                                                listOfMaterias = updatedListOfMaterias
//                                            )
//                                            viewModel.updateUser(user?.id ?: 110, modifiedUser, requireContext())
//
//                                            viewModel.updateMateria(materiaId, materiaUser, requireContext())
//                                            viewModel.getMateriaById(materiaId, requireContext())
//                                        }
//                                        isModalVisible = false
//                                    },
//
//                                    ) {
//                                    Text(text = "Eliminar")
//                                }
//                                Button(onClick = {
//
//
//                                    isModalVisible = false
//                                }) {
//                                    Text(text = "Cancelar")
//
//                                }
//                            }
//
//                        }
//                    }
//                }
//            )
//        }
    }

    @Composable
    fun ListItemAsignacion(item: Actividad) {
        // Replace with your list item implementation
        // You can use Card, ListItem, or any other Composable to represent a single item in the list
        var isModalVisible by remember {
            mutableStateOf(false)
        }
        var isProgressModalLoading by remember {
            mutableStateOf(false)
        }

        var isButtonEnabled by remember { mutableStateOf(true) }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
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
//
//            Text(text = item.id,
//            modifier = Modifier.padding(16.dp),
//                color = Color.LightGray
//            )
                IconButton(onClick = { isModalVisible = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }

        }

        if (isProgressModalLoading) {
            Dialog(
                onDismissRequest = { isProgressModalLoading = false },
                content = {

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = colorResource(id = R.color.blue_dark),


                            )
                    }
                }
            )
        }

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
                                    if (isButtonEnabled){
                                        isButtonEnabled = false
                                        isProgressModalLoading = true


                                        item.id?.let { viewModel.deleteActivity(it, requireContext()).observe(viewLifecycleOwner){
                                            if (it){
                                                isProgressModalLoading = false
                                                isButtonEnabled = true

                                            }else{
                                                isProgressModalLoading = false
                                                isButtonEnabled = true


                                            }
                                        } }
                                        isButtonEnabled = true
                                        isModalVisible = false

                                    }

                                }) {
                                    Text(text = "Eliminar")
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContentAsignacion(listOfActivities: MutableLiveData<List<Actividad>>) {
        val actividades by listOfActivities.observeAsState(initial = emptyList())

        var refresh = viewModel.refreshingActividadById.observeAsState()
        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getActivitiesById(
            viewModel.currentMateria.value?.id ?: 1000, requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{

            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    .padding(5.dp)){
                LazyColumn {
                    items(actividades) { actividad ->
                        ListItemAsignacion(item = actividad)
                    }
                }
                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabsAsignacionWithPagerScreen() {
        var selectedTabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Listado", "Tareas", "Sexo", "edad")


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
                .padding(4.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
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
                        verticalArrangement = Arrangement.Top
                    ) {
                        ListContentUsersSuscribed(userList = viewModel.currentMateria)
                    }
                    1 -> ListContentAsignacion(listOfActivities = viewModel.activitiesListById)
                    3 -> GenderPerformanceChartByMateria(viewModel = viewModel)
                    4 -> AgeRangePerformanceChart(viewModel = viewModel, context = requireContext())

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



    @Composable
    fun MateriaContent(){

        var button1 by remember { mutableStateOf(true) }
        var button2 by remember { mutableStateOf(false) }
        var button3 by remember { mutableStateOf(false) }
        var button4 by remember { mutableStateOf(false) }

        var isModalVisible by remember { mutableStateOf(false) }
        var isModalAsignacionesVisible by remember { mutableStateOf(false) }
        var tituloAsignacion by remember { mutableStateOf(String()) }
        var descripcionAsignacion by remember { mutableStateOf(String()) }
        var isButtonEnabled by remember { mutableStateOf(true) }
        var isProgressModalLoading by remember {
            mutableStateOf(false)
        }

        val selectedUsers = viewModel.currentMateria.observeAsState().value?.listStudent?.toMutableList()
        var materiaId = viewModel.currentMateria.value?.id

        Log.e("selected users in general", selectedUsers.toString())
        Column(
            verticalArrangement = Arrangement.Top
        ) {
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

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.blue_dark),
                            contentColor = Color.White
                        ),
                        onClick = { isModalVisible = true  }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                            Text(text = "Agregar", modifier = Modifier.padding(horizontal = 2.dp))
                            Icon(painterResource(id = R.drawable.ic_person_add), contentDescription = null)
                        }
                    }
                }else{
                    Text(
                        text = "Asignaciones",
                        style = MaterialTheme.typography.body2,
                        fontSize = 30.sp,
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.blue_dark),
                            contentColor = Color.White
                        ),
                        onClick = { isModalAsignacionesVisible = true  }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,

                        ) {
                            Text(text = "Agregar", modifier = Modifier.padding(horizontal = 2.dp))
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
//                    IconButton(onClick = { isModalAsignacionesVisible = true }) {
////                    val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
//                        Icon(
//                            imageVector = Icons.Default.Add,
//                            contentDescription = "QR Code",
//                            modifier = Modifier.size(40.dp)
//                        )
//                    }
                }


            }
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
//                    TabsAsignacionWithPagerScreen()
//                    ListContentAsignacionGeneralEstudiante(listOfActivities = viewModel.activitiesListById)

                Button(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)                    ),
                    onClick = {
                        button1 = true
                        button2 = false
                        button3 = false
                        button4 = false
                    },

                    ) {
                    Text(text = "Alumnos", style = MaterialTheme.typography.subtitle1,
                        fontSize = 14.sp,
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)                    ),
                    onClick = {
                        button2 = true
                        button1 = false
                        button3 = false
                        button4 = false

                    },
                ) {
                    Text(text = "Tareas", style = MaterialTheme.typography.subtitle1, fontSize = 14.sp)
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)                    ),
                    onClick = {
                        button2 = false
                        button1 = false
                        button3 = true
                        button4 = false

                    },
                ) {
                    Text(text = "Sexo", style = MaterialTheme.typography.subtitle1, fontSize = 14.sp)
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)
                    ),
                    onClick = {
                        button2 = false
                        button1 = false
                        button3 = false
                        button4 = true

                    },
                ) {
                    Text(text = "Edad", style = MaterialTheme.typography.subtitle1, fontSize = 14.sp)
                }

            }

//            TabsAsignacionWithPagerScreen()


//            Row(
//                modifier = Modifier.padding(4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = {
//                        button1 = true
//                        button2 = false
//                        button3 = false
//
//                    },
//                    modifier = Modifier
//                        .weight(1F)
//                        .background(colorResource(id = R.color.accent)),
//
//                    ) {
//                    Text(text = "Alumnos", style = MaterialTheme.typography.subtitle1)
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Button(
//                    onClick = {
//                        button2 = true
//                        button1 = false
//                        button3 = false
//
//                    },
//                    modifier = Modifier
//                        .weight(1F)
//                        .background(colorResource(id = R.color.accent))
//                ) {
//                    Text(text = "Asignaciones", style = MaterialTheme.typography.subtitle1)
//                }
//                Button(
//                    onClick = {
//                        button3 = true
//                        button1 = false
//                        button2 = false
//
//                    },
//                    modifier = Modifier
//                        .weight(1F)
//                        .background(colorResource(id = R.color.accent))
//                ) {
//                    Text(text = "Rendimiento", style = MaterialTheme.typography.subtitle1)
//                }
//
//            }

            if (button1) {
                ListContentUsersSuscribed(userList = viewModel.currentMateria)
            } else if (button2) {
                ListContentAsignacion(listOfActivities = viewModel.activitiesListById)
            }else if(button3){
                GenderPerformanceChartByMateria(viewModel = viewModel)
            }else{
                AgeRangePerformanceChartByMateria(viewModel = viewModel, context = requireContext())
            }

            if (isProgressModalLoading) {
                Dialog(
                    onDismissRequest = { isProgressModalLoading = false },
                    content = {

                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                                color = colorResource(id = R.color.blue_dark),


                                )
                        }
                    }
                )
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

                                    ListContentUsers(userList = viewModel.userStudentsList,
                                        selectedUsers = selectedUsers?: mutableListOf(),
                                        userListSuscribed = viewModel.currentMateria
                                    )

                                }
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.blue_dark),
                                        contentColor = Color.White,
                                    ),
                                    enabled = isButtonEnabled,
                                    onClick = {
                                        if(isButtonEnabled){
                                            isProgressModalLoading = true
                                            isButtonEnabled = false
                                            val currentMateria = viewModel.currentMateria.value
                                            if (currentMateria != null) {
                                                val materiaId = currentMateria.id
                                                val teacherId = currentMateria.idTeacher
                                                val materiaName = currentMateria.name

                                                if (selectedUsers != null) {
                                                    for (user in selectedUsers) {
                                                        // Create a modified user object with additional data
                                                        val materiaUser = Materia(
                                                            id = currentMateria.id,
                                                            name = currentMateria.name,
                                                            idTeacher = currentMateria.idTeacher
                                                        )

                                                        val updatedListOfMaterias = user.listOfMaterias.orEmpty().toMutableList()

                                                        // Check if the materia with the same id already exists
                                                        val existingMateria = updatedListOfMaterias.find { it.id == materiaUser.id }

                                                        if (existingMateria == null) {
                                                            // If the materia does not exist, add it to the list
                                                            updatedListOfMaterias.add(materiaUser)
                                                        } else {
                                                            // If the materia already exists, you can handle it as needed (skip, update, etc.)
                                                            // For now, let's just print a message
                                                            println("Materia with id ${materiaUser.id} already exists for user ${user.id}")
                                                        }
                                                        val modifiedUser = User(
                                                            id = user.id, // Replace with the actual property name for the user ID
                                                            firstName = user.firstName,
                                                            lastName = user.lastName,
                                                            email = user.email,
                                                            password = user.password,
                                                            gender = user.gender,
                                                            edad = user.edad,
                                                            rol = user.rol,
                                                            birthday = user.birthday,
                                                            imageProfile = user.imageProfile,
                                                            phone = user.phone,
                                                            cedula = user.cedula,
                                                            listActivities = user.listActivities,
                                                            lgn = user.lgn,
                                                            lat = user.lat,
                                                            listOfMaterias = updatedListOfMaterias,
                                                            created = user.created

                                                        )

                                                        // Call viewModel.updateUser for each user in the list
                                                        viewModel.updateUser(user?.id ?: 110, modifiedUser, requireContext()).observe(viewLifecycleOwner){
                                                            if(it){
                                                                isButtonEnabled = true
                                                                isProgressModalLoading = false


                                                            }else{
                                                                isButtonEnabled = true
                                                                isProgressModalLoading = false


                                                            }
                                                        }

                                                        // Update the materia with the modified user list
                                                    }
                                                }

                                                viewModel.updateMateria(materiaId, Materia(materiaId, teacherId, selectedUsers, materiaName), requireContext())
                                                isButtonEnabled = true
                                                isProgressModalLoading = false
                                                isModalVisible = false
                                            }
                                        }

                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(text = "Agregar")
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

                                        Button(
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = colorResource(id = R.color.blue_dark),
                                                contentColor = Color.White,
                                            ),
                                            onClick = {
                                            val newFragment = DatePickerFragment()
                                            newFragment.show(childFragmentManager, "datePicker")
                                        }) {
                                            Text(text = "Fecha")
                                        }
                                    }


                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.blue_dark),
                                        contentColor = Color.White,
                                    ),
                                    onClick = {

                                        if (!tituloAsignacion.isNullOrEmpty() && !descripcionAsignacion.isNullOrEmpty() && !viewModel?.birthday?.value.isNullOrEmpty()){
                                            isProgressModalLoading = true

                                            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES"))
                                            var isDateFormatValid = true

                                            try {
                                                dateFormat.parse(viewModel.birthday.value)
                                            } catch (e: ParseException) {
                                                isDateFormatValid = false
                                                isProgressModalLoading = false

                                            }

                                            if (isDateFormatValid){
                                                viewModel.createActivity(
                                                    Actividad(
                                                        idClass = viewModel.currentMateria.value!!.id,
                                                        title = tituloAsignacion,
                                                        description = descripcionAsignacion,
                                                        date = viewModel.birthday.value,

                                                        ),
                                                    requireContext()
                                                )
                                                isModalAsignacionesVisible = false
                                                isProgressModalLoading = false

                                                tituloAsignacion = ""
                                                descripcionAsignacion = ""
                                                viewModel.birthday.postValue("Fecha")

                                            }else{
                                                viewModel.showToast("Coloca una fecha", requireContext())
                                                isProgressModalLoading = false

                                            }

                                            if (materiaId != null) {
                                                viewModel.getActivitiesById(materiaId, requireContext())
                                            }

                                        }else{
                                            viewModel.showToast("Rellene todos los campos", requireContext())
                                            isProgressModalLoading = false

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

        }
    }
}