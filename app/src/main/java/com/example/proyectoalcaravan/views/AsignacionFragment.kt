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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
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
        Log.d("FragmentLifecycle", "Fragment created: ${javaClass.simpleName}")

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
        var isProgressModalLoading by remember { mutableStateOf(false) }
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.calificationRevision.toString())
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(painterResource(id = R.drawable.ic_check_circle), contentDescription = "algo")

                    }
                } else if (item.messageStudent != null && item.imageRevision != null){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Añadir nota")
                        Spacer(modifier = Modifier.width(2.dp))
                        IconButton(onClick = { isModalNotaVisible = true}) {
                            Icon(painterResource(id = R.drawable.ic_add_task), contentDescription = "algo")
                        }
                    }
                }else{
                    Text(text = "Por entregar")
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

                                        if(!item.messageStudent.isNullOrBlank()){
                                            Text(
                                                text = "Mensaje adjunto: ${item.messageStudent}",
                                                modifier = Modifier.padding(bottom = 2.dp, top = 7.dp),
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                        }

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
                                                    isProgressModalLoading = true
                                                    if (user.isNotNull() && !notaAsignacion.isNullOrEmpty()) {
                                                        val notaValue = notaAsignacion.toIntOrNull()


                                                        if (notaValue == null || notaValue > 100){
                                                            isButtonEnabled = true
                                                            isProgressModalLoading = false


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
                                                                    isProgressModalLoading = false


                                                                }
                                                            }else{
                                                                isButtonEnabled = true
                                                                isProgressModalLoading = false

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
                                                                listOfMaterias = user?.listOfMaterias,
                                                                created = user?.created
                                                            )

                                                            viewModel.updateUserAsignacion(user?.id ?: 110, updateUser, requireContext())
                                                                .observe(viewLifecycleOwner){
                                                                    if (it){
                                                                        user?.id?.let { viewModel.getUserById(it, requireContext()) }
                                                                        isModalNotaVisible = false
                                                                        isProgressModalLoading = false

                                                                    }else{
                                                                        isButtonEnabled = true
                                                                        isProgressModalLoading = false


                                                                    }
                                                                }

                                                        }



                                                    }else{
                                                        isButtonEnabled = true
                                                        isProgressModalLoading = false
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
        var isProgressModalLoading by remember { mutableStateOf(false) }
        var rememberedItem = remember { item }

        var isModalNotaVisible by remember { mutableStateOf(false) }
        var notaAsignacion by remember { mutableStateOf(String()) }
        var mensaje by remember { mutableStateOf(String()) }
        var modalVisible by remember { mutableStateOf(false) }
//        val user by viewModel.updatedUser.observeAsState()
        var updatedUser = viewModel.updatedUser.observeAsState()
        var updatedUserCompose = viewModel.updatedUserCompose

        var currentUser = viewModel.currentUser.value
        var currentUserDB = viewModel.currentUserDB.value

        val profileImageUri by viewModel.profileImage.observeAsState()

        val isModalOpen by viewModel.modalAsignacion1.observeAsState()


        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            isModalNotaVisible = true
                            viewModel.modalAsignacion1.postValue(true)
                            Log.e("item", item.toString())
                            viewModel.currentActividad.postValue(item)
                        }
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
                            Icon(painterResource(id = R.drawable.ic_adjuntar), contentDescription = "algo")
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

        if (isModalNotaVisible){
//        if (isModalOpen == true){


            Dialog(
                onDismissRequest = { isModalNotaVisible = false },
//                onDismissRequest = { viewModel.modalAsignacion1.postValue(false) },

                content = {        Log.e("algo", "algo")


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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        enabled = isButtonEnabled,
                                        onClick = {
                                            if(isButtonEnabled){
                                                isButtonEnabled = false
                                                isProgressModalLoading = true
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

                                                if ( !mensaje.isNullOrEmpty()){

//                                        if (viewModel.currentActividad.value?.messageStudent.isNotNull() && !mensaje.isNullOrEmpty()){
                                                    // Define a reference to Firebase Storage
                                                    if(viewModel.currentActividad.value?.messageStudent.isNullOrEmpty()){

//                                            if(item.messageStudent.isNullOrEmpty()){
                                                        viewModel.showToast("Ha ocurrido un error", requireContext())
                                                        isButtonEnabled = true
                                                        isProgressModalLoading = false


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
                                                                    lgn = user.lgn,
                                                                    lat = user.lat,
                                                                    listOfMaterias = user.listOfMaterias,
                                                                    created = user.created

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
                                                                            isProgressModalLoading = false

                                                                            viewModel.listWithActivities()
                                                                            viewModel.showToast("Se ha enviado la evaluación correctamente", requireContext())

                                                                        }else{
                                                                            isButtonEnabled = true
                                                                            isProgressModalLoading = false

                                                                            viewModel.showToast("No se ha enviado la evaluacion", requireContext())

                                                                        }
                                                                    }



                                                            }.addOnFailureListener { exception ->
                                                                // Handle the error while trying to get the download URL
                                                                Log.e("firebase error", "Storage Error: ${exception.message}")
                                                                isProgressModalLoading = false

                                                            }
                                                        }.addOnFailureListener { exception ->
                                                            // Handle the error during image upload
                                                            if (exception is StorageException) {
                                                                // Handle storage-specific errors
                                                                isProgressModalLoading = false

                                                                isButtonEnabled = true

                                                                Log.e("firebase error", "Storage Error: ${exception.message}")
                                                            } else {
                                                                // Handle other non-storage-related exceptions
                                                                isButtonEnabled = true
                                                                isProgressModalLoading = false


                                                                Log.e("firebase error", "Storage Error: ${exception.message}")
                                                            }
                                                        }
                                                    }

                                                }else{
                                                    isButtonEnabled = true
                                                    isProgressModalLoading = false


                                                    viewModel.showToast("Llene todos los campos!!", requireContext())
                                                }
                                            }




                                        }

                                    ) {
                                        Text(text = "Enviar evaluación")
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Button(onClick = { isModalNotaVisible = false }) {
                                        Text(text = "Cerrar")
                                    }
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


//
//        LaunchedEffect(key1 = viewModel.currentMateria.observeAsState().value ){
//            viewModel.listWithActivities()
//
//        }
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


//            Box(modifier = Modifier.pullRefresh(pullRefreshState)){
        Box(
            modifier = Modifier.padding(4.dp)
        ){
            LazyColumn {
                    items(activities ?: viewModel.listOfActivitiesFiltered.value ?: emptyList()) { actividad ->

//                    items(viewModel.listOfActivitiesFilteredCompose ?: viewModel.listOfActivitiesFiltered.value ?: emptyList()) { actividad ->



                        if (user != null) {
                            ListItemAsignacion(item = actividad, user = user)
                        }
                    }
                }
//                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

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
                                                        listOfMaterias = user?.listOfMaterias,
                                                        created = user?.created

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

        viewModel.currentMateria.value?.id?.let {
            viewModel.updateListWithActivitiesById(it, requireContext())
            viewModel.getActivitiesById(it, requireContext()) }


        var filteredList = viewModel.listOfActivitiesFiltered.observeAsState()
        var filteredListCompose = viewModel.listOfActivitiesFilteredCompose


        var refresh = viewModel.refreshingActividadById.observeAsState()



        Log.e("filteredList", filteredList.value.toString())
        Log.e("filteredList", filteredListCompose.toString())




        viewModel.currentMateria.value?.id?.let { viewModel.listWithActivities() }
        viewModel.listWithActivities()


        LaunchedEffect(key1 = viewModel.currentMateria.observeAsState().value ){
            viewModel.listWithActivities()

        }


        // Pager state
        val pagerState = rememberPagerState(pageCount = {tabs.size})
        val coroutineScope = rememberCoroutineScope()

        // Observe the current page index and update the selectedTabIndex accordingly
        LaunchedEffect(pagerState.currentPage) {
            selectedTabIndex = pagerState.currentPage
        }
        val nota = viewModel.listOfActivitiesFilteredCompose?.let {
            viewModel.calculateOverallCalification(
                it
            )
        }
        Log.e("nota", nota.toString())

//        var nota by remember { mutableStateOf(0.0) }
//
//        val actividades by viewModel.listOfActivitiesFiltered.observeAsState()
//
//        LaunchedEffect(key1 = true){
//            viewModel.listWithActivities()
//
//            actividades?.let {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//         }
//        }
//
//        var nota4 = viewModel.listOfActivitiesFilteredCompose.let {
//            if (it != null) {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//            }
//        }
//
//        LaunchedEffect(key1 = true){
//
//            nota = viewModel.listOfActivitiesFilteredCompose?.let {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//            }!!
//        }
//
//        var nota2 = viewModel.listOfActivitiesFilteredCompose?.let {
//            viewModel.calculateOverallCalification(
//                it
//            )
//        }

        Log.e("nota", nota.toString())
//        val formattedNumber2 = String.format("%.2f", viewModel.currentNota.observeAsState().value)

        val formattedNumber = String.format("%.2f", nota)
//        val formattedNumber3 = String.format("%.2f", nota2)

//        Log.e("Nota 4", nota4.toString())


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
                        if(nota != null){
                            Text(text = "Nota General: ${formattedNumber}%", fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                        listsOfActivities()
                    }
                    1 ->  if(viewModel.currentUser.observeAsState().value?.rol == "Estudiante"){
                        Column {
//                            Text(text = "Nota General: ${nota.toString()}")
//                            Spacer(modifier = Modifier.height(5.dp))
                            LazyColumn{
                                item{
                                    LineChart2(viewModel = viewModel)

                                }
                                item{
                                    LineChart(viewModel = viewModel)

                                }
                            }



                        }

//                        LineChart(viewModel = viewModel)
                    }else{
                        Column {
//                            Text(text = "Nota General: ${nota.toString()}")
//                            Spacer(modifier = Modifier.height(5.dp))
                            LazyColumn{
                                item{
                                    LineChart2(viewModel = viewModel)

                                }
                                item{
                                    LineChart(viewModel = viewModel)

                                }
                            }


                        }

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
    fun listsOfActivities() {
        var currentUser = viewModel.currentUser.observeAsState()
        var currentUserDB = viewModel.currentUserDB.observeAsState()
        var user = viewModel.updatedUser

        viewModel.currentMateria.value?.let {
            viewModel.getActivitiesById(it.id, requireContext())
            viewModel.listWithActivities() }
        viewModel.listWithActivities()


        LaunchedEffect(key1 = viewModel.currentMateria.observeAsState().value ){
            viewModel.listWithActivities()

        }

        val nota = viewModel.listOfActivitiesFilteredCompose?.let {
            viewModel.calculateOverallCalification(
                it
            )
        }
        Log.e("nota", nota.toString())

//        var nota by remember { mutableStateOf(0.0) }
//
//        val actividades by viewModel.listOfActivitiesFiltered.observeAsState()
//
//        LaunchedEffect(key1 = true){
//            viewModel.listWithActivities()
//
//            actividades?.let {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//         }
//        }
//
//        var nota4 = viewModel.listOfActivitiesFilteredCompose.let {
//            if (it != null) {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//            }
//        }
//
//        LaunchedEffect(key1 = true){
//
//            nota = viewModel.listOfActivitiesFilteredCompose?.let {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//            }!!
//        }
//
//        var nota2 = viewModel.listOfActivitiesFilteredCompose?.let {
//            viewModel.calculateOverallCalification(
//                it
//            )
//        }

        Log.e("nota", nota.toString())
//        val formattedNumber2 = String.format("%.2f", viewModel.currentNota.observeAsState().value)

        val formattedNumber = String.format("%.2f", nota)

//        viewModel.currentMateria.value?.id?.let { viewModel.listWithActivities() }
//        viewModel.listWithActivities()
//
//
//        LaunchedEffect(key1 = viewModel.currentMateria.observeAsState().value ){
//            viewModel.listWithActivities()
//            viewModel.listOfActivitiesFilteredCompose?.let {
//                viewModel.calculateOverallCalification(
//                    it
//                )
//            }
//
//
//        }
////        LaunchedEffect(key1 = true ){
////            viewModel.listWithActivities()
////            viewModel.listOfActivitiesFilteredCompose?.let {
////                viewModel.calculateOverallCalification(
////                    it
////                )
////            }
////
////
////        }
//        val nota2 by viewModel.currentNota.observeAsState()
//        val nota3 = viewModel.currentNotaCompose
//
//        val nota4 = viewModel.listOfActivitiesFiltered.value?.let {
//            viewModel.calculateOverallCalification(
//                it
//            )
//        }
//
//
//        val nota = viewModel.listOfActivitiesFilteredCompose?.let {
//            viewModel.calculateOverallCalification(
//                it
//            )
//        }
//        Log.e("nota", nota.toString())
//
//
//        Log.e("nota", nota.toString())
//
////        val formattedNumber = String.format("%.2f", nota)
//        val formattedNumber = String.format("%.2f", nota)


        Log.e("List with actividades", viewModel.listOfActivitiesFilteredCompose.toString())

        if (user != null) {
            if (currentUser.value?.rol == "Profesor" || currentUserDB.value?.rol == "Profesor"){
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    if(nota != null){
                        Text(text = "Nota General: ${formattedNumber}%", fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 5.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    ListContentAsignacion(user = user)
                }
            }else{
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    if(nota != null){
                        Text(text = "Nota General: ${formattedNumber}%", fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 5.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    ListContentAsignacionGeneralEstudiante(listOfActivities = viewModel.activitiesListById)
                }
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
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
verticalAlignment = Alignment.CenterVertically                ) {
//                    TabsAsignacionWithPagerScreen()
//                    ListContentAsignacionGeneralEstudiante(listOfActivities = viewModel.activitiesListById)

                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)
                    ),
                    onClick = {
                        button1 = true
                        button2 = false
                    },

                    ) {
                    Text(text = "General", style = MaterialTheme.typography.subtitle1,
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.blue_dark),
                        backgroundColor = colorResource(id = R.color.accent2)
                    ),
                    onClick = {
                        button2 = true
                        button1 = false
                    },
                ) {
                    Text(text = "Metricas", style = MaterialTheme.typography.subtitle1)
                }

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
            if(button1){
                listsOfActivities()
            }else{
                if (currentUser?.rol == "Estudiante" || currentUserDB?.rol == "Estudiante"){
                    LineChart(viewModel = viewModel)
                }else{
                    LineChart2(viewModel = viewModel)

                }

//                LineChart(viewModel = viewModel)
            }


        }

    }



}