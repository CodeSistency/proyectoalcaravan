package com.example.proyectoalcaravan.views

import android.annotation.SuppressLint
//import android.graphics.Color
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
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.utils.generateRandomColor
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.ColorList
import com.example.proyectoalcaravan.views.componentes.shimmer.ProfileShimmer
import com.example.proyectoalcaravan.views.profesor.ProfesorFragmentDirections
import com.example.proyectoalcaravan.views.student.StudentFragmentDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    val args: ProfileFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                var user = viewModel.currentUser.observeAsState()
                var refreshing = viewModel.refreshingUpdatedUser.observeAsState()
                var refreshingCurrent = viewModel.refreshingCurrentUser.observeAsState()

                var userDB = viewModel.currentUserDB.observeAsState()
                var updatedUser = viewModel.updatedUser.observeAsState()

                viewModel.prueba()
//                if(args.profile != 3000){
//                    Log.e("profile", args.profile.toString())
//                        Log.e("profile test", args.profile.toString())
//                        viewModel.getUserById(args.profile, requireContext())
//                    if(refreshing.value == true){
//                        ProfileShimmer()
//
////                        Profile(viewModel.currentUser.observeAsState().value)
//                    }else{
////                        ProfileShimmer()
//                        Profile(viewModel.currentUser.observeAsState().value)
//
//                    }
//
//                }
//                if(refreshingCurrent.value == true){
////                    Profile(viewModel.currentUser.observeAsState().value)
//                    ProfileShimmer()
//
//                }else{
////                    ProfileShimmer()
//                    Profile(viewModel.currentUser.observeAsState().value)
//
//                }
//                Profile(viewModel.currentUser.observeAsState().value)

                    if(args.profile != 3000){
                        Log.e("profile", args.profile.toString())
                        Log.e("profile test", args.profile.toString())
                        viewModel.getUserById(args.profile, requireContext())

                    }
                    Profile(viewModel.currentUser.observeAsState().value)

            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProfileCard(currentUser: User?, userDB: UserDB?) {

        var updatedUser = viewModel.updatedUser.observeAsState()
        var userDatabase = viewModel.currentUserDB.observeAsState()

        Column {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    color = colorResource(id = R.color.blue_dark),
                    shape = RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp),

                    )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Profile Image
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White),

                ) {
                    if (args.profile != 3000){
                        if(updatedUser.value != null){
                            Log.e("updated user test", updatedUser.toString())
                            GlideImage(
                                model = updatedUser.value?.imageProfile,
                                contentDescription = "foto",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(60.dp),
                                contentScale = ContentScale.Fit
                            )
                        }else{
                            
                        }
                        
                    }else{
                        GlideImage(
                            model = userDatabase.value?.imageProfile ?: currentUser?.imageProfile,
                            contentDescription = "foto",
                            modifier = Modifier
                                .fillMaxSize()
                                .size(60.dp),
                            contentScale = ContentScale.Fit
                        )
                    }


                }

                Spacer(modifier = Modifier.height(16.dp))

                if (args.profile != 3000){
                    
                    if(updatedUser.value != null){
                        Log.e("updated user test", updatedUser.toString())

                        Text(
                            text = updatedUser.value?.firstName?: "nombre",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = updatedUser.value?.lastName?: "apellido",
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                    }else{
                        Text(
                            text = "Usuario no encontrado",
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }


                }else{
                    Text(
                        text = userDB?.firstName ?: currentUser?.firstName ?: "Nombre",
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = userDB?.lastName ?: currentUser?.lastName ?: "Apellido",
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }




                Spacer(modifier = Modifier.weight(1f))
            }
        }
        }
    }

    @Composable
    fun SmallMap(currentUser: User?, userDB: UserDB?) {
        val context = LocalContext.current
        var updatedUser = viewModel.updatedUser.observeAsState()


        Card(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize()
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { MapView(context) }
            ) { mapView ->
                mapView.onCreate(null)
                mapView.onResume()

                mapView.getMapAsync { googleMap ->
                    // Customize the map as needed
                    googleMap.uiSettings.isZoomControlsEnabled = true

                    // Add a marker to the map (you can customize this part)
                    val markerOptions = MarkerOptions()
                        .position(LatLng(37.7749, -122.4194)) // Replace with your desired location
                        .title("Marker Title")
                    googleMap.addMarker(markerOptions)

                    // Move the camera to the desired location


                    if (args.profile != 3000){


                            var cameraPosition = CameraPosition.Builder()
                                .target(LatLng(updatedUser.value?.lat ?: 10.0000, updatedUser.value?.lat ?: 10.0000)) // Replace with your desired location
                                .zoom(15f) // Zoom level
                                .build()
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


                    }else{
                        var cameraPosition = CameraPosition.Builder()
                            .target(LatLng(userDB?.lat ?:currentUser?.lat ?: 0.078867, userDB?.lgn ?: currentUser?.lgn ?: 0.078867)) // Replace with your desired location
                            .zoom(15f) // Zoom level
                            .build()
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }



                }
            }

        // Create a MapView composable

        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun HorizontalList(gridItems: List<Materia>?) {
//        val items by gridItems.observeAsState(initial = emptyList())

        LazyRow(
            modifier = Modifier
//                    .fillMaxWidth()
                .padding(8.dp)

        ) {
            items(gridItems ?: emptyList()) { item ->
                HorizontalItemCard(item = item)
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
                .border(1.dp, colorResource(id = R.color.blue_dark), RoundedCornerShape(16.dp))


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

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Profile(currentUser: User?) {
        var user = viewModel.currentUser.observeAsState()
        var isModalVisible by remember { mutableStateOf(false) }
        var userDB = viewModel.currentUserDB.observeAsState()
        var updatedUser = viewModel.updatedUser.observeAsState()
        Log.e("profile, userDB", userDB.value.toString())
        Log.e("profile, user", user.value.toString())
        Log.e("profile, updated User", updatedUser.value.toString())

        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()




        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Perfil") },
                    backgroundColor = colorResource(id = R.color.accent),
                    navigationIcon = {
                        IconButton(onClick = {
                            if(isClickable){
                                isClickable = false
                                view?.findNavController()?.popBackStack()

                                // Launch a coroutine to re-enable clickable after a delay
                                coroutineScope.launch {
                                    delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                    isClickable = true
                                }
                            }

                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (args.profile != 3000){
                                null
                            }else{
                                IconButton(onClick = {
                                    if(isClickable){
                                        isClickable = false
                                        isModalVisible = true

                                        // Launch a coroutine to re-enable clickable after a delay
                                        coroutineScope.launch {
                                            delay(2000) // Adjust the delay duration as needed (in milliseconds)
                                            isClickable = true
                                        }
                                    }
//                                if (userDB.value != null) {
//                                    viewModel.deleteUserDB(UserDB(userDB.value!!.id, userDB.value!!.userId, userDB.value!!.firstName, userDB.value!!.lastName, userDB.value!!.birthday, userDB.value!!.cedula, userDB.value!!.gender, userDB.value!!.imageProfile, userDB.value!!.email, userDB.value!!.password, userDB.value!!.rol, userDB.value!!.phone, userDB.value!!.lgn, userDB.value!!.lag, userDB.value!!.listActivities, userDB.value!!.listOfMaterias) )
//                                }
//                                viewModel.loggedIn.postValue(false)
//                                view?.findNavController()?.navigate(R.id.action_profileFragment_to_login)
                                }
                                )
                                {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_logout),
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        }
                    }
                )
            }
        ) {
           
            LazyColumn{
                item {
                    ProfileCard(currentUser, userDB.value)

                }
                item {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))

                        if (args.profile != 3000){
                            if(updatedUser.value != null){
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Email:",
                                        style = MaterialTheme.typography.h3,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 20.dp),
                                        fontSize = 23.sp

                                    )
                                    Spacer(modifier = Modifier.width(5.dp))

                                    OutlinedButton(
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(id = R.color.blue_dark),
                                            backgroundColor = Color.White
                                        ),
                                        onClick = { /*TODO*/ }) {
                                        Text(text = "${updatedUser.value?.email ?: "Email"}")
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Cedula:",
                                        style = MaterialTheme.typography.h3,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 20.dp),
                                        fontSize = 23.sp

                                    )
                                    Spacer(modifier = Modifier.width(5.dp))

                                    OutlinedButton(
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(id = R.color.blue_dark),
                                            backgroundColor = Color.White

                                        ),
                                        onClick = { /*TODO*/ }) {
                                        Text(text = "${updatedUser.value?.cedula ?: "Cedula"}")
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Telefono:",
                                        style = MaterialTheme.typography.h3,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 20.dp),
                                        fontSize = 23.sp

                                    )
                                    Spacer(modifier = Modifier.width(5.dp))

                                    OutlinedButton(
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = colorResource(id = R.color.blue_dark),
                                            backgroundColor = Color.White

                                        ),
                                        onClick = { /*TODO*/ }) {
                                        Text(text = "0${updatedUser.value?.phone ?: "Telefono"}")
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
//
                            }else{
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "Usuario no encontrado")
                                }
                            }



                        }else if(userDB.isNotNull() || currentUser.isNotNull()){
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Email:",
                                    style = MaterialTheme.typography.h3,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 20.dp),
                                    fontSize = 23.sp

                                )
                                Spacer(modifier = Modifier.width(5.dp))

                                OutlinedButton(
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(id = R.color.blue_dark),
                                        backgroundColor = Color.White

                                    ),
                                    onClick = { /*TODO*/ }) {
                                    Text(text = "${currentUser?.email ?: userDB?.value?.email ?: "Email"}")
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Cedula:",
                                    style = MaterialTheme.typography.h3,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 20.dp),
                                    fontSize = 23.sp

                                )
                                Spacer(modifier = Modifier.width(5.dp))

                                OutlinedButton(
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(id = R.color.blue_dark),
                                        backgroundColor = Color.White

                                    ),
                                    onClick = { /*TODO*/ }) {
                                    Text(text = "${currentUser?.cedula ?: userDB?.value?.cedula ?: "Cedula"}")
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Telefono:",
                                    style = MaterialTheme.typography.h3,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 20.dp),
                                    fontSize = 23.sp

                                )
                                Spacer(modifier = Modifier.width(5.dp))

                                OutlinedButton(
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = colorResource(id = R.color.blue_dark),
                                        backgroundColor = Color.White

                                    ),
                                onClick = { /*TODO*/ }) {
                                    Text(text = "0${currentUser?.phone ?: userDB?.value?.phone ?: "Telefono"}")
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))






                        }


                    }
                    Spacer(modifier = Modifier.height(10.dp))



                    if (args.profile != 3000){
                        HorizontalList(gridItems = viewModel.updatedUser.observeAsState().value?.listOfMaterias)

                    }else{
                        HorizontalList(gridItems = viewModel.currentUser.observeAsState().value?.listOfMaterias)

                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Text(text = "Ubicación",
                            fontSize = 25.sp,
                            )
                       Icon(painterResource(id = R.drawable.ic_location), contentDescription = null)
                    }
                    SmallMap(currentUser, userDB.value)
//                    if (args.profile != 3000){
//                        if (updatedUser != null){
//                            SmallMap(currentUser, userDB.value)
//                        }else{
//                            Text(text = "Ubicación no encontrada")
//                        }
//
//                    }else if(currentUser != null){
//                        SmallMap(currentUser, userDB.value)
//                    }
                }

            }

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
                                text = "¿Estás seguro que quiere salir?",
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
                                    if (userDB.value != null) {
                                        viewModel.deleteUserDB(UserDB(userDB.value!!.id, userDB.value!!.userId, userDB.value!!.firstName, userDB.value!!.lastName, userDB.value!!.birthday, userDB.value!!.cedula, userDB.value!!.edad, userDB.value!!.gender, userDB.value!!.imageProfile, userDB.value!!.email, userDB.value!!.password, userDB.value!!.rol, userDB.value!!.phone, userDB.value!!.lgn, userDB.value!!.lat, userDB.value!!.created, userDB.value!!.listActivities, userDB.value!!.listOfMaterias) )
                                    }
                                    viewModel.loggedIn.postValue(false)
                                    view?.findNavController()?.navigate(R.id.action_profileFragment_to_login)
                                    isModalVisible = false
                                }) {
                                    Text(text = "Salir")
                                }
                            }
                        }
                    }
                }
            )
        }
    }


}