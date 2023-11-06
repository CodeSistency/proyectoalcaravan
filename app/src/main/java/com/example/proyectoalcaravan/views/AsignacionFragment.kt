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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import co.yml.charts.common.extensions.isNotNull
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.Header2
import com.google.accompanist.coil.rememberCoilPainter
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage


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
        var mensaje by remember { mutableStateOf(String()) }

        var user = viewModel.updatedUser.value



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



//                if(user?.rol == "Profesor"){
//                    Column {
//                        IconButton(onClick = { isModalNotaVisible = true}) {
//                            Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
//                        }
//                        if (item.isCompleted){
//                            Text(text = item.calificationRevision.toString())
//                        }
//                    }
//
//                }else{
//                    Column {
//                        if (item.isCompleted){
//                            Text(text = item.calificationRevision.toString())
//                        }else{
//                            Text(text = "Por revisar")
//                        }
//                    }
//                }
                IconButton(onClick = { isModalNotaVisible = true}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "algo")
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
//                        if(user.isNotNull() && user?.rol == "Estudiante"){
//                            Column {
//                               IconButton(onClick = {
//                                   val dialog = ImagePickerDialogFragment()
//                                   dialog.show(childFragmentManager, "image_picker_dialog")
//                               }) {
//                                   Icon(imageVector = Icons.Default.Add, contentDescription = "AÑADIR EVALUACION")
//                               }
//                                Column {
//                                    Text(text = "Mensaje")
//                                    OutlinedTextField(value = mensaje,
//                                        onValueChange = {
//                                            mensaje = it
//                                        },
//
//                                        placeholder = {
//                                            Text(text = "Añade alguna acotación")
//                                        },
//                                        keyboardOptions = KeyboardOptions.Default.copy(
//                                            keyboardType = KeyboardType.Number
//                                        )
//
//                                    )
//                                    Button(onClick = {
//
//                                        val evaluacion = Actividad(
//                                            calification = item.calification,
//                                            calificationRevision = item.calificationRevision,
//                                            date = item.date,
//                                            description = item.description,
//                                            id = item.id,
//                                            idClass = item.idClass,
//                                            imageRevision = item.imageRevision,
//                                            isCompleted = item.isCompleted,
//                                            messageStudent = mensaje,
//                                            title = item.title
//                                        )
//                                        val updateUser = User(
//                                            id = user.id, // Replace with the actual property name for the user ID
//                                            firstName = user.firstName,
//                                            lastName = user.lastName,
//                                            email = user.email,
//                                            password = user.password,
//                                            gender = user.gender,
//                                            rol = user.rol,
//                                            birthday = user.birthday,
//                                            imageProfile = user.imageProfile,
//                                            phone = user.phone,
//                                            cedula = user.cedula,
//                                            listActivities = user.listActivities?.plus(
//                                                evaluacion
//                                            ),
//                                            lgn = user.lgn,
//                                            lat = user.lat,
//                                            listOfMaterias = user.listOfMaterias
//
//                                        )
//
//                                        viewModel.updateUser(user?.id ?: 110, updateUser)
//                                    }
//
//                                    ) {
//                                        Text(text = "Enviar evaluación")
//                                    }
//                                }
//                            }
//                        }else{
//                        if (item.imageRevision.isNullOrEmpty()){
//                            Text(text = "Todavía no se ha enviado la asignación")
//                        } else{
//                            //Validar que no puede poner nota porque no ha llegado la evaluacion
//                        }

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
                                                }
                                            },

                                            placeholder = {
                                                Text(text = "Coloque la nota")
                                            },
                                            keyboardOptions = KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Number
                                            )

                                        )
                                        Button(onClick = {



                                            if (user.isNotNull() && !notaAsignacion.isNullOrEmpty()) {

                                                val evaluacion = Actividad(
                                                    calification = notaAsignacion.toInt(),
                                                    calificationRevision = item.calificationRevision,
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
                                                    }
                                                }

                                                val updateUser = User(
                                                    id = user?.id,
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
                                                    listActivities = updatedListOfActivities,
                                                    lgn = user?.lgn,
                                                    lat = user?.lat,
                                                    listOfMaterias = user?.listOfMaterias
                                                )

                                                viewModel.updateUser(user?.id ?: 110, updateUser)
                                                user?.id?.let { viewModel.getUserById(it) }

                                        }else{
                                            viewModel.showToast("Coloque una nota", requireContext())
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

        LazyColumn {
            items(actividades) { actividad ->
                ListItemAsignacionGeneral(item = actividad)
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ListItemAsignacionGeneral(item: Actividad) {
        var modalVisible by remember { mutableStateOf(false) }
        val user by viewModel.updatedUser.observeAsState()
        var mensaje by remember { mutableStateOf(String()) }
        val profileImageUri by viewModel.profileImage.observeAsState()







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
                if (user?.rol == "Estudiante"){
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
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )

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



                                                    viewModel.updateUser(user?.id ?: 110, updateUser)
                                                    user?.id?.let { viewModel.getUserById(it) }
                                                    viewModel.profileImage.postValue(null)
                                                    viewModel.getAllUsers()
                                                    modalVisible = false

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

    @Composable
    fun ListContentAsignacion(user: User?) {
        val actividades by rememberUpdatedState(user?.listActivities ?: emptyList())


        LazyColumn {
            items(actividades) { actividad ->
                if (actividad != null) {
                    ListItemAsignacion(item = actividad)
                }
            }
        }
    }

    @Composable
    fun AsignacionContent(){
        var button1 by remember { mutableStateOf(true) }
        var button2 by remember { mutableStateOf(false) }

        var user = viewModel.updatedUser.value

        Column {
            Header2(titulo = "Asignaciones", viewModel.currentMateria?.value?.name ?: "", )

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
                    Text(text = "General", style = MaterialTheme.typography.subtitle1)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        button2 = true
                        button1 = false
                    },
                    modifier = Modifier.weight(1F)
                ) {
                    Text(text = "Entregas", style = MaterialTheme.typography.subtitle1)
                }

            }

            if (button1) {
                ListContentAsignacionGeneral(listOfActivities = viewModel.activitiesListById)
            } else {
                ListContentAsignacion(user = user)
            }

        }

    }


}