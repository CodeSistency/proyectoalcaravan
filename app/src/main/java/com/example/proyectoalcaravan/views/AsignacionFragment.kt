package com.example.proyectoalcaravan.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import co.yml.charts.common.extensions.isNotNull
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.isOnline
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.charts.LineChart
import com.example.proyectoalcaravan.views.charts.LineChart2
import com.example.proyectoalcaravan.views.componentes.Header2
import com.example.proyectoalcaravan.views.componentes.TimedMessage
import com.example.proyectoalcaravan.views.componentes.connection.NoInternetMessage
import com.example.proyectoalcaravan.views.componentes.shimmer.ShimmerCardList
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch


class AsignacionFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    val args:AsignacionFragmentArgs by navArgs()
    val storage = Firebase.storage("gs://login-android-e42b8.appspot.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            Log.e("arg", args.student.toString())
            viewModel.getUserById(args.student, requireContext())
            (viewModel.currentUser.value?.id ?: viewModel.currentUserDB.value?.userId)?.let {
                viewModel.getUserRefresh(
                    it,
                    requireContext()
                )
            }
            viewModel.currentMateria.value?.id?.let { viewModel.getActivitiesById(it, requireContext()) }
            setContent {
                AsignacionContent()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserById(args.student, requireContext())
        viewModel.currentMateria.value?.id?.let { viewModel.getActivitiesById(it, requireContext()) }
        Log.e("onResume activitiesListByIdCompose", viewModel.activitiesListByIdCompose.toString())
        Log.e("onResume activitiesListById", viewModel.activitiesListById.value.toString())
    }

    override fun onStop() {
        super.onStop()

//        viewModel.updatedUser.postValue(null)
//        viewModel.updatedUserCompose = null
//
//        viewModel.activitiesListById.postValue(emptyList())
//        viewModel.activitiesListByIdCompose = emptyList()

        Log.e("stop activitiesListByIdCompose", viewModel.activitiesListByIdCompose.toString())
        Log.e("stop activitiesListById", viewModel.activitiesListById.value.toString())
        Log.e("stop user new", viewModel.updatedUser.value.toString())

    }

    override fun onPause() {
        super.onPause()

//        viewModel.updatedUser.postValue(null)
//        viewModel.updatedUserCompose = null
//
//        viewModel.activitiesListById.postValue(emptyList())
//        viewModel.activitiesListByIdCompose = emptyList()

        Log.e("pause activitiesListByIdCompose", viewModel.activitiesListByIdCompose.toString())
        Log.e("pause activitiesListById", viewModel.activitiesListById.value.toString())
        Log.e("stop user new", viewModel.updatedUser.value.toString())



    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.updatedUser.postValue(null)
        viewModel.updatedUserCompose = null

        viewModel.activitiesListById.postValue(emptyList())
        viewModel.activitiesListByIdCompose = emptyList()

        Log.e("destroy activitiesListById", viewModel.activitiesListById.value.toString())
        Log.e("destroy activitiesListByIdCompose", viewModel.activitiesListByIdCompose.toString())
        Log.e("stop user new", viewModel.updatedUser.value.toString())


    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacion(item: Actividad) {
        // Replace with your list item implementation
        var isButtonEnabled by remember { mutableStateOf(true) }

        var isModalNotaVisible by remember { mutableStateOf(false) }
        var notaAsignacion by remember { mutableStateOf(String()) }
        var mensaje by remember { mutableStateOf(String()) }
        var currentUser = viewModel.currentUser.value
        var currentUserDB = viewModel.currentUserDB.value


        var user = viewModel.updatedUser.value

        LaunchedEffect(currentUser, isModalNotaVisible){
            Log.e("asignacion", currentUser.toString())
        }

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
                if(item.isCompleted){
                    Text(text = item.calificationRevision.toString())
                } else if (item.messageStudent != null && item.imageRevision != null){
                    IconButton(onClick = { isModalNotaVisible = true}) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
                    }
                }else{
                    Text(text = "Por entregar")
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

                            .drawWithContent {
                                drawContent()

//                                    drawShadow(16.dp, shape = RoundedCornerShape(16.dp))
                            },
                        elevation = 8.dp
                    ) {


                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    GlideImage(
                                        model = item.imageRevision,
                                        contentDescription = "foto",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                    )

                                    Column {
                                        Text(
                                            text = "Calificación: ",
                                            modifier = Modifier.padding(bottom = 2.dp, top = 7.dp),
                                            fontSize = 20.sp
                                        )
                                        OutlinedTextField(value = notaAsignacion,
                                            onValueChange = {
                                                if (it.all { char -> char.isDigit() }) {
                                                    notaAsignacion = it

                                                    Log.e("changin", item.toString())
                                                }
                                            },

                                            placeholder = {
                                                Text(text = "Coloque la nota")
                                            },
                                            keyboardOptions = KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Number
                                            )

                                        )
                                        Button(
                                            enabled = isButtonEnabled,
                                            onClick = {
                                                if(isButtonEnabled){
                                                    isButtonEnabled = false
                                                    if (user.isNotNull() && !notaAsignacion.isNullOrEmpty()) {
                                                        val notaValue = notaAsignacion.toIntOrNull()


                                                        if (notaValue == null || notaValue > 100){
                                                            isButtonEnabled = true

                                                            viewModel.showToast("Nota invalida", requireContext())
                                                        }else{
                                                            Log.e("nota string", notaAsignacion.toString())
                                                            val evaluacion = Actividad(
                                                                calification = 100,
                                                                calificationRevision = notaAsignacion.toInt(),
                                                                date = item.date,
                                                                description = item.description,
                                                                id = item.id,
                                                                idClass = item.idClass,
                                                                imageRevision = item.imageRevision,
                                                                isCompleted = true,
                                                                messageStudent = item.messageStudent,
                                                                title = item.title
                                                            )

                                                            val updatedListOfActivities = user?.listActivities?.toMutableList()
                                                            val index = updatedListOfActivities?.indexOfFirst { it?.id == evaluacion.id }

                                                            if (index != -1) {
                                                                if (index != null) {
                                                                    updatedListOfActivities?.set(index, evaluacion)
                                                                }else{
                                                                    isButtonEnabled = true

                                                                }
                                                            }else{
                                                                isButtonEnabled = true

                                                            }

                                                            val updateUser = User(
                                                                id = user?.id,
                                                                firstName = user?.firstName,
                                                                lastName = user?.lastName,
                                                                email = user?.email,
                                                                password = user?.password,
                                                                edad = user?.edad,
                                                                gender = user?.gender,
                                                                rol = user?.rol,
                                                                birthday = user?.birthday,
                                                                imageProfile = user?.imageProfile,
                                                                phone = user?.phone,
                                                                cedula = user?.cedula,
                                                                listActivities = updatedListOfActivities,
                                                                lgn = user?.lgn,
                                                                lat = user?.lat,
                                                                listOfMaterias = user?.listOfMaterias
                                                            )

                                                            viewModel.updateUserAsignacion(user?.id ?: 110, updateUser, requireContext())
                                                                .observe(viewLifecycleOwner){
                                                                    if (it){
                                                                        user?.id?.let { viewModel.getUserById(it, requireContext()) }
                                                                        isModalNotaVisible = false
                                                                    }else{

                                                                    }
                                                                }

                                                        }



                                                    }else{
                                                        isButtonEnabled = true
                                                        viewModel.showToast("Coloque una nota", requireContext())
                                                    }
                                                }

                                        }

                                        ) {
                                            Text(text = "Enviar nota")
                                        }
                                    }
                                }


//                        }


                    }
                }
            )
        }
    }

    @Composable
    fun ListContentAsignacionGeneral(listOfActivities: MutableLiveData<List<Actividad>>) {
        val actividades by listOfActivities.observeAsState(initial = emptyList())
        Log.e("actividades", actividades.toString())

        LazyColumn {
            items(actividades) { actividad ->
                ListItemAsignacionGeneral(item = actividad)
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacion(item: Actividad, user: User) {
        var isButtonEnabled by remember { mutableStateOf(true) }
        var isAlertOpen by remember { mutableStateOf(null) }
        var rememberedItem = remember { item }

        var isModalNotaVisible by remember { mutableStateOf(false) }
        var notaAsignacion by remember { mutableStateOf(String()) }
        var mensaje by remember { mutableStateOf(String()) }
        var modalVisible by remember { mutableStateOf(false) }
//        val user by viewModel.updatedUser.observeAsState()
        var updatedUser = viewModel.updatedUser.observeAsState()
        var updatedUserCompose = viewModel.updatedUserCompose

//        val itemState = rememberUpdatedState(item)
//
//        var evaluacion = Actividad(
//            calification = 100,
//            calificationRevision = itemState.value.calificationRevision,
//            date = itemState.value.date,
//            description = itemState.value.description,
//            id = itemState.value.id,
//            idClass = itemState.value.idClass,
//            imageRevision = itemState.value.imageRevision,
//            isCompleted = itemState.value.isCompleted,
//            messageStudent = mensaje,
//            title = itemState.value.title
//        )


        var currentUser = viewModel.currentUser.value
        var currentUserDB = viewModel.currentUserDB.value

        val profileImageUri by viewModel.profileImage.observeAsState()

        val isModalOpen by viewModel.modalAsignacion1.observeAsState()






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
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.padding(bottom = 2.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fecha: ${item.date}",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(bottom = 2.dp),
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                }

                if (item.isCompleted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.calificationRevision.toString(),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                } else if (!item.imageRevision.isNullOrBlank()) {
                    Text(text = "Entregado")
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Enviar")
                        Spacer(modifier = Modifier.width(5.dp))
                        IconButton(
                            onClick = {
                                isModalNotaVisible = true
                                viewModel.modalAsignacion1.postValue(true)
                                Log.e("item", item.toString())
                                viewModel.currentActividad.postValue(item)

//                                rememberedItem = item
//                                Log.e("item remember", rememberedItem.toString())
//                                Log.e("item remember", evaluacion.toString())

                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
                        }
                    }
                }




            }
        }



//        if (isModalNotaVisible){
        if (isModalOpen == true){


            Dialog(
//                onDismissRequest = { modalVisible = false },
                onDismissRequest = { viewModel.modalAsignacion1.postValue(false) },

                content = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(245, 245, 245),
                                shape = RoundedCornerShape(16.dp),
                            )
                            .drawWithContent {
                                drawContent()

//                                    drawShadow(16.dp, shape = RoundedCornerShape(16.dp))
                            },
                        elevation = 8.dp
                    ){
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Manda la asignación",
                                modifier = Modifier.padding(5.dp),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {
                                val dialog = ImagePickerDialogFragment()
                                dialog.show(childFragmentManager, "image_picker_dialog")
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .border(width = 1.dp, color = Color.LightGray)
                            ) {
                                if (profileImageUri == null){
                                    Icon(
                                        painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "AÑADIR EVALUACION",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .scale(1F),
                                        tint = Color.Gray


                                    )
                                }else{

                                    Image(
                                        painter = rememberAsyncImagePainter(model = profileImageUri),
                                        contentDescription = "Profile Image",
                                        modifier = Modifier.fillMaxSize()
                                    )

                                }

                            }
                            Column {
                                Text(
                                    text = "Mensaje adjunto: ",
                                    modifier = Modifier.padding(bottom = 2.dp, top = 7.dp),
                                    fontSize = 20.sp
                                )
                                OutlinedTextField(value = mensaje,
                                    onValueChange = {
                                        mensaje = it
                                    },

                                    placeholder = {
                                        Text(text = "Añade alguna acotación")
                                    },


                                    )
                                Button(
                                    enabled = isButtonEnabled,
                                    onClick = {
                                    if(isButtonEnabled){
                                        isButtonEnabled = false
//
//
                                        Log.e("item", item.toString())


//                                        val updatedListActivities = user?.listActivities?.let { list ->
//                                            if (!list.any { it?.id == evaluacion?.id }) {
//                                                list.plus(evaluacion)
//                                            } else {
//                                                list
//                                            }
//                                        } ?: listOf(evaluacion)
                                        viewModel.currentActividad.value?.messageStudent = mensaje

                                        if (viewModel.currentActividad.value?.messageStudent.isNotNull() && !mensaje.isNullOrEmpty()){
                                            // Define a reference to Firebase Storage
                                            if(item.messageStudent.isNullOrEmpty()){
                                                viewModel.showToast("Ha ocurrido un error", requireContext())
                                                isButtonEnabled = true

                                            }else{
                                                val storageRef = storage.reference.child("${user?.email}.jpg")

                                                // Upload the image to Firebase Storage
                                                val uploadTask = storageRef.putFile(profileImageUri!!)

                                                uploadTask.addOnSuccessListener { taskSnapshot ->
                                                    // The image has been successfully uploaded
                                                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                        val downloadUri = uri.toString()
//                                                        item?.imageRevision = downloadUri
//                                                        item?.calification = 100


                                                        viewModel.currentActividad.value?.imageRevision = downloadUri



                                                        val updatedListActivities = user?.listActivities?.toMutableList() ?: mutableListOf()

                                                        if (!updatedListActivities.any { it?.id == viewModel.currentActividad.value?.id }) {
                                                            updatedListActivities.add(viewModel.currentActividad.value)
                                                        }

//                                                        item.messageStudent = mensaje

                                                        val updatedUserAsignacion = User(
                                                            id = user?.id, // Replace with the actual property name for the user ID
                                                            firstName = user?.firstName,
                                                            lastName = user?.lastName,
                                                            email = user?.email,
                                                            password = user?.password,
                                                            edad = user?.edad,
                                                            gender = user?.gender,
                                                            rol = user?.rol,
                                                            birthday = user?.birthday,
                                                            imageProfile = user?.imageProfile,
                                                            phone = user?.phone,
                                                            cedula = user?.cedula,
//                                                            listActivities = user?.listActivities?.plus(
//                                                                viewModel.currentActividad.value
//                                                            ),
                                                            listActivities = updatedListActivities,
//                                                            listActivities = user?.listActivities?.plus(evaluacion)?.distinctBy { it?.id } ?: listOf(evaluacion),

//                                                            listActivities = updatedListActivities,
                                                            lgn = user?.lgn,
                                                            lat = user?.lat,
                                                            listOfMaterias = user?.listOfMaterias

                                                        )

//                                                        Log.e("list of updated", updatedListActivities.toString())


                                                        Log.e("user asignacion before", updatedUser.value.toString())

                                                        viewModel.updateUserAsignacion(user?.id ?: 110,
                                                            updatedUserAsignacion, requireContext())
                                                            .observe(viewLifecycleOwner){
                                                                if(it){
                                                                    updatedUser.value?.let {
                                                                        mensaje = ""
//                                                        viewModel.updateUser(user?.id ?: 110,
//                                                            it, requireContext())

                                                                        Log.e("user asignacion after", it.toString())
                                                                    }
                                                                    viewModel.updatedUser.postValue(updatedUserAsignacion)
                                                                    user?.id?.let { viewModel.getUserById(it, requireContext()) }
                                                                    viewModel.profileImage.postValue(null)
//                                                    profileImageUri = null
                                                                    viewModel.getAllUsers(requireContext())
                                                                    modalVisible = false
                                                                    viewModel.modalAsignacion1.postValue(false)
                                                                    isButtonEnabled = true
                                                                    viewModel.listWithActivities()
                                                                    viewModel.showToast("it works", requireContext())

                                                                }else{
                                                                    isButtonEnabled = true
                                                                    viewModel.showToast("No se ha enviado la evaluacion", requireContext())

                                                                }
                                                            }



                                                    }.addOnFailureListener { exception ->
                                                        // Handle the error while trying to get the download URL
                                                        Log.e("firebase error", "Storage Error: ${exception.message}")
                                                    }
                                                }.addOnFailureListener { exception ->
                                                    // Handle the error during image upload
                                                    if (exception is StorageException) {
                                                        // Handle storage-specific errors
                                                        isButtonEnabled = true

                                                        Log.e("firebase error", "Storage Error: ${exception.message}")
                                                    } else {
                                                        // Handle other non-storage-related exceptions
                                                        isButtonEnabled = true

                                                        Log.e("firebase error", "Storage Error: ${exception.message}")
                                                    }
                                                }
                                            }

                                        }else{
                                            isButtonEnabled = true

                                            viewModel.showToast("Llene todos los campos!!", requireContext())
                                        }
                                    }




                                }

                                ) {
                                    Text(text = "Enviar evaluación")
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
    fun ListContentAsignacionGeneralEstudiante(listOfActivities: MutableLiveData<List<Actividad>>) {
        val actividades by listOfActivities.observeAsState(initial = emptyList())
        Log.e("actividades", actividades.toString())
        var user = viewModel.updatedUser.observeAsState().value

        viewModel.currentMateria.value?.id?.let {
            viewModel.updateListWithActivitiesById(it, requireContext())
            viewModel.getActivitiesById(it, requireContext()) }


        var filteredList = viewModel.listOfActivitiesFiltered.observeAsState()
        var filteredListCompose = viewModel.listOfActivitiesFilteredCompose


        var refresh = viewModel.refreshingActividadById.observeAsState()

        fun listWithActivities(): List<Actividad> {
            return listOfActivities.value?.map { existingActivity ->
                user?.listActivities?.find { it?.id == existingActivity.id } ?: existingActivity
            } ?: emptyList()
        }

        val updatedList = listOfActivities.value?.map { existingActivity ->
            user?.listActivities?.find { it?.id == existingActivity.id } ?: existingActivity
        } ?: emptyList()

        Log.e("filteredList", filteredList.value.toString())
        Log.e("filteredList", filteredListCompose.toString())




        viewModel.currentMateria.value?.id?.let { viewModel.listWithActivities() }
        viewModel.listWithActivities()


        LaunchedEffect(key1 = viewModel.currentMateria.observeAsState().value ){
            viewModel.listWithActivities()

        }
        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, {
            listWithActivities()
            user?.id?.let {
            viewModel.currentMateria.value?.id?.let { viewModel.updateListWithActivitiesById(it, requireContext()) }

            viewModel.getAllMaterias(requireContext())
            viewModel.getActivitiesById(viewModel.currentMateria.value?.id ?: 1000, requireContext())
            viewModel.getUserById(
                it, requireContext())
        } })

        Log.e("Test of the list", updatedList.toString())

        val activities by viewModel.listOfActivitiesFiltered.observeAsState()


            Box(modifier = Modifier.pullRefresh(pullRefreshState)){
                LazyColumn {
                    items(activities ?: viewModel.listOfActivitiesFiltered.value ?: emptyList()) { actividad ->

//                    items(viewModel.listOfActivitiesFilteredCompose ?: viewModel.listOfActivitiesFiltered.value ?: emptyList()) { actividad ->



                        if (user != null) {
                            ListItemAsignacion(item = actividad, user = user)
                        }
                    }
                }
                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

            }


    }



    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacionGeneral(item: Actividad) {
        var modalVisible by remember { mutableStateOf(false) }
//        val user by viewModel.updatedUser.observeAsState()
        var user = viewModel.updatedUser.observeAsState().value
        var currentUser = viewModel.currentUser.observeAsState().value
        var currentUserDB = viewModel.currentUserDB.observeAsState().value

        var mensaje by remember { mutableStateOf(String()) }
        val profileImageUri by viewModel.profileImage.observeAsState()

        LaunchedEffect(user, modalVisible){
            Log.e("user role", user?.rol.toString())
            Log.e("user in asignacion", user.toString())

        }

//        LaunchedEffect(key1 = true){
//            viewModel.currentMateria.value?.id?.let { viewModel.getActivitiesById(it, requireContext()) }
//        }

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


                if (currentUserDB?.rol == "Profesor"){
                    Text(text = item.calificationRevision.toString())
                }else{
                    IconButton(onClick = { modalVisible = true}) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
                    }
                }





//



            }


            if (modalVisible){

                Dialog(
                    onDismissRequest = { modalVisible = false },
                    content = {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(245, 245, 245),
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .drawWithContent {
                                    drawContent()

//                                    drawShadow(16.dp, shape = RoundedCornerShape(16.dp))
                                },
                            elevation = 8.dp
                        ){
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Manda la asignación",
                                    modifier = Modifier.padding(5.dp),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = {
                                    val dialog = ImagePickerDialogFragment()
                                    dialog.show(childFragmentManager, "image_picker_dialog")
                                },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .border(width = 1.dp, color = Color.LightGray)
                                    ) {
                                    if (profileImageUri == null){
                                        Icon(
                                            painterResource(id = R.drawable.ic_camera),
                                            contentDescription = "AÑADIR EVALUACION",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .scale(1F),
                                            tint = Color.Gray


                                        )
                                    }else{

                                        Image(
                                            painter = rememberAsyncImagePainter(model = profileImageUri),
                                            contentDescription = "Profile Image",
                                            modifier = Modifier.fillMaxSize()
                                        )

                                    }

                                }
                                Column {
                                    Text(
                                        text = "Mensaje adjunto: ",
                                        modifier = Modifier.padding(bottom = 2.dp, top = 7.dp),
                                        fontSize = 20.sp
                                        )
                                    OutlinedTextField(value = mensaje,
                                        onValueChange = {
                                            mensaje = it
                                        },

                                        placeholder = {
                                            Text(text = "Añade alguna acotación")
                                        },


                                    )
                                    Button(onClick = {


                                        var evaluacion = Actividad(
                                            calification = item.calification,
                                            calificationRevision = item.calificationRevision,
                                            date = item.date,
                                            description = item.description,
                                            id = item.id,
                                            idClass = item.idClass,
                                            imageRevision = item.imageRevision,
                                            isCompleted = item.isCompleted,
                                            messageStudent = mensaje,
                                            title = item.title
                                        )

                                        if (profileImageUri.isNotNull() && !mensaje.isNullOrEmpty()){
                                            // Define a reference to Firebase Storage
                                            val storageRef = storage.reference.child("${user?.email}.jpg")

                                            // Upload the image to Firebase Storage
                                            val uploadTask = storageRef.putFile(profileImageUri!!)

                                            uploadTask.addOnSuccessListener { taskSnapshot ->
                                                // The image has been successfully uploaded
                                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                    val downloadUri = uri.toString()
                                                    evaluacion?.imageRevision = downloadUri

                                                    val updateUser = User(
                                                        id = user?.id, // Replace with the actual property name for the user ID
                                                        firstName = user?.firstName,
                                                        lastName = user?.lastName,
                                                        email = user?.email,
                                                        password = user?.password,
                                                        gender = user?.gender,
                                                        rol = user?.rol,
                                                        birthday = user?.birthday,
                                                        imageProfile = user?.imageProfile,
                                                        phone = user?.phone,
                                                        cedula = user?.cedula,
                                                        listActivities = user?.listActivities?.plus(
                                                            evaluacion
                                                        ),
                                                        lgn = user?.lgn,
                                                        lat = user?.lat,
                                                        listOfMaterias = user?.listOfMaterias

                                                    )



                                                    viewModel.updateUser(user?.id ?: 110, updateUser, requireContext())
                                                        .observe(viewLifecycleOwner){
                                                        if(it){
                                                            user?.id?.let { it1 ->
                                                                viewModel.getUserById(
                                                                    it1, requireContext())
                                                                viewModel.profileImage.postValue(null)
                                                                mensaje = ""
                                                                modalVisible = false

                                                            }

                                                        }else{
                                                            user?.id?.let { viewModel.getUserById(it, requireContext()) }
//                                                    profileImageUri = null
                                                            viewModel.getAllUsers(requireContext())
                                                        }
                                                    }


                                                }.addOnFailureListener { exception ->
                                                    // Handle the error while trying to get the download URL
                                                    Log.e("firebase error", "Storage Error: ${exception.message}")
                                                }
                                            }.addOnFailureListener { exception ->
                                                // Handle the error during image upload
                                                if (exception is StorageException) {
                                                    // Handle storage-specific errors
                                                    Log.e("firebase error", "Storage Error: ${exception.message}")
                                                } else {
                                                    // Handle other non-storage-related exceptions
                                                    Log.e("firebase error", "Storage Error: ${exception.message}")
                                                }
                                            }
                                        }else{
                                            viewModel.showToast("Llene todos los campos!!", requireContext())
                                        }



                                    }

                                    ) {
                                        Text(text = "Enviar evaluación")
                                    }
                                }

                            }



                        }


                    }

                )

            }

        }


    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListContentAsignacion(user: MutableLiveData<User?>) {
//        user.value?.id?.let { viewModel.getUserById(it, requireContext()) }
        val activities by user.observeAsState()
        val activitiesCurrentUser by viewModel.currentUser.observeAsState()

        // Assuming viewModel.currentMateria.value is a LiveData
        val currentMateriaId by viewModel.currentMateria.observeAsState()

        var activitiesFiltered = activities?.listActivities?.filter { actividad ->
            actividad?.idClass == currentMateriaId?.id
        } as MutableList<Actividad>? ?: mutableListOf()



        var refresh = viewModel.refreshingUpdatedUser.observeAsState()
        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, {
            user.value?.id?.let { viewModel.getUserById(it, requireContext()) }
            viewModel.getAllMaterias(requireContext())
            viewModel.getActivitiesById(viewModel.currentMateria.value?.id ?: 1000, requireContext())
        })

        if (refresh.value == true) {
            ShimmerCardList()
        }else if(activitiesFiltered.isEmpty()){

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No hay actividades disponibles")
            }
        } else {
            Box(Modifier.pullRefresh(pullRefreshState)) {
                LazyColumn {
                    items(activitiesFiltered ?: emptyList()) { actividad ->
                        if (actividad != null) {
                            ListItemAsignacion(item = actividad)
                        }
                    }
                }
            }
        }
    }

//    @OptIn(ExperimentalMaterialApi::class)
//    @Composable
//    fun ListContentAsignacion(listOfActivities: MutableLiveData<List<Actividad>>, user: MutableLiveData<User?>) {
//        val activities by user.observeAsState()
//        val activitiesCurrentUser by viewModel.currentUser.observeAsState()
//        val currentMateriaId by viewModel.currentMateria.observeAsState()
//        var refresh = viewModel.refreshingUpdatedUser.observeAsState()
//
//        // Assuming viewModel.currentMateria.value is a LiveData
//        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, {
//            user.value?.id?.let { viewModel.getUserById(it, requireContext()) }
//            viewModel.getAllMaterias(requireContext())
//            viewModel.getActivitiesById(viewModel.currentMateria.value?.id ?: 1000, requireContext())
//        })
//
//        val listActivities = activities?.listActivities ?: emptyList()
//
//        // Filter and modify the list of activities
//        val activitiesFiltered = listActivities.map { activity ->
//            val existingActivity = listOfActivities.value?.find { it.id == activity?.id }
//            if (existingActivity != null) {
//                // Replace properties based on conditions
//                existingActivity.copy(
//                    calification = activity?.calification ?: 100,
//                    calificationRevision = activity?.calificationRevision ?: existingActivity.calificationRevision,
//                    date = activity?.date,
//                    description = activity?.description ?: existingActivity.description,
//                    isCompleted = activity?.isCompleted ?: existingActivity.isCompleted,
//                    messageStudent = if (activity?.messageStudent != null && activity.messageStudent != "") activity.messageStudent else existingActivity.messageStudent,
//                    imageRevision = if (activity?.imageRevision != null && activity.imageRevision != "") activity.imageRevision else existingActivity.imageRevision,
//                    title = if (activity?.title != null && activity?.title != "") activity.title else existingActivity.title
//                )
//            } else {
//                activity
//            }
//        }
//
//        if (refresh.value == true) {
//            ShimmerCardList()
//        } else {
//            Box(Modifier.pullRefresh(pullRefreshState)) {
//                LazyColumn {
//                    items(activitiesFiltered) { actividad ->
//                        if (actividad != null) {
//                            ListItemAsignacion(item = actividad)
//                        }
//                    }
//                }
//            }
//        }
//    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabsAsignacionWithPagerScreen() {
        var selectedTabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Asignaciones", "Metricas")


        // Pager state
        val pagerState = rememberPagerState(pageCount = {tabs.size})
        val coroutineScope = rememberCoroutineScope()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
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
                    .weight(1f)
            ) { page ->
                // Content for each tab
                when (page) {
                    0 -> listsOfActivities()
                    1 ->  if(viewModel.currentUser.observeAsState().value?.rol == "Estudiante"){
                        LineChart(viewModel = viewModel)
                    }else{
                        LineChart2(viewModel = viewModel)

                    }

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
                .background(if (isSelected) Color.Gray else Color.Transparent)
                .clickable { onTabClick() }
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(8.dp)
                    .background(if (isSelected) Color.Gray else Color.Transparent)
            )
        }
    }

    @Composable
    fun listsOfActivities() {
        var currentUser = viewModel.currentUser.observeAsState()
        var currentUserDB = viewModel.currentUserDB.observeAsState()
        var user = viewModel.updatedUser


        if (user != null) {
            if (currentUser.value?.rol == "Profesor" || currentUserDB.value?.rol == "Profesor"){
                ListContentAsignacion(user = user)
            }else{
                ListContentAsignacionGeneralEstudiante(listOfActivities = viewModel.activitiesListById)
//                user.value?.let { ListContentAsignacionGeneral(listOfActivities = viewModel.activitiesListById, user = it) }

            }
        }
    }

    @Composable
    fun AsignacionContent(){
        var button1 by remember { mutableStateOf(true) }
        var button2 by remember { mutableStateOf(false) }
        var currentUser = viewModel.currentUser.value
        var currentUserDB = viewModel.currentUserDB.value

        LaunchedEffect(key1 = true ){
            currentUserDB?.userId?.let { viewModel.getUserRefresh(it, requireContext()) }
        }

        LaunchedEffect(key1 = true ){
            viewModel.currentMateria.value?.let { viewModel.getActivitiesById(viewModel.currentMateria.value!!.id, requireContext()) }
        }

        var user = viewModel.updatedUser.value

        Column {
            Header2(titulo = "Asignaciones", viewModel.currentMateria?.observeAsState()?.value?.name ?: "", )

            if (isOnline(requireContext())){
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TabsAsignacionWithPagerScreen()
//                    ListContentAsignacionGeneralEstudiante(listOfActivities = viewModel.activitiesListById)

//                Button(
//                    onClick = {
//                        button1 = true
//                        button2 = false
//                    },
//                    modifier = Modifier.weight(1F),
//
//                    ) {
//                    Text(text = "General", style = MaterialTheme.typography.subtitle1)
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Button(
//                    onClick = {
//                        button2 = true
//                        button1 = false
//                    },
//                    modifier = Modifier.weight(1F)
//                ) {
//                    Text(text = "Metricas", style = MaterialTheme.typography.subtitle1)
//                }

                }
            }else{
                NoInternetMessage()
            }


//            if (button1) {
//                if (user != null) {
//                    if (currentUser?.rol == "Profesor" || currentUserDB?.rol == "Profesor"){
//                        ListContentAsignacion(user = user)
//                    }else{
//                        ListContentAsignacionGeneral(listOfActivities = viewModel.activitiesListById, user = user)
//
//                    }
//                }
//            } else {
////                ListContentAsignacion(user = user)
//            }

        }

    }



}