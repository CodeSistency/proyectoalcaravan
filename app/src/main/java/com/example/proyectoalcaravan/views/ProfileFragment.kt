package com.example.proyectoalcaravan.views

import android.annotation.SuppressLint
//import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ProfileFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    val args: ProfileFragmentArgs by navArgs()

    val pointsData: List<Point> =
        listOf(Point(0f, 40f), Point(1f, 90f), Point(2f, 0f), Point(3f, 60f), Point(4f, 10f))

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Gray)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(7)
        .backgroundColor(Color.Gray)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = 100 / 7F
            (i * yScale).formatToSinglePrecision()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )
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
                if(args.isNotNull()){
                    viewModel.getUserById(args.profile)
                }
                Profile(viewModel.currentUser.value)
//                MyFragmentContent(viewModel)
//                Button(onClick = { checkCamaraPermissions() }) {
//                    Text(text = "Scanner")
//                }
//
//                Text(text = "${textResult}")
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProfileCard(currentUser: User?, userDB: UserDB?) {

        var updatedUser = viewModel.updatedUser.observeAsState()
        Column {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = colorResource(id = R.color.mostaza),
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
                    if (args.isNotNull()){
                        GlideImage(
                            model = updatedUser.value?.imageProfile,
                            contentDescription = "foto",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }else{
                        GlideImage(
                            model = userDB?.imageProfile ?: currentUser?.imageProfile,
                            contentDescription = "foto",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }


                }

                Spacer(modifier = Modifier.height(16.dp))

                if (args.isNotNull()){

                    Text(
                        text = updatedUser.value?.firstName?: "nombre",
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = updatedUser.value?.lastName?: "apellido",
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }else{
                    Text(
                        text = userDB?.firstName ?: currentUser?.firstName.toString(),
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = userDB?.lastName ?: currentUser?.lastName.toString(),
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


        // Create a MapView composable
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


                if (args.isNotNull()){
                    var cameraPosition = CameraPosition.Builder()
                        .target(LatLng(updatedUser.value?.lat ?: 10.0000, updatedUser.value?.lat ?: 10.0000)) // Replace with your desired location
                        .zoom(15f) // Zoom level
                        .build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                }else{
                    var cameraPosition = CameraPosition.Builder()
                        .target(LatLng(userDB?.lag ?:currentUser?.lat ?: 0.078867, userDB?.lgn ?: currentUser?.lgn ?: 0.078867)) // Replace with your desired location
                        .zoom(15f) // Zoom level
                        .build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }



            }
        }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Profile(currentUser: User?) {
        var userDB = viewModel.currentUserDB.value
        var updatedUser = viewModel.updatedUser.observeAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Perfil") },
                    backgroundColor = MaterialTheme.colors.primary,
                    navigationIcon = {
                        IconButton(onClick = { view?.findNavController()?.popBackStack()

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
                            IconButton(onClick = {view?.findNavController()?.navigate(ProfileFragmentDirections.actionProfileFragmentToRegisterStepTwo2(true,currentUser?.id?: 1000)) }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_editar),
                                    contentDescription = "Settings"
                                )
                            }
                            IconButton(onClick = {
                                if (userDB != null) {
                                    viewModel.deleteUserDB(UserDB(userDB.id, userDB.userId, userDB.firstName, userDB.lastName, userDB.birthday, userDB.cedula, userDB.gender, userDB.imageProfile, userDB.email, userDB.password, userDB.rol, userDB.phone, userDB.lgn, userDB.lag, userDB.listActivities, userDB.listOfMaterias) )
                                }
                                view?.findNavController()?.navigate(R.id.action_profileFragment_to_login) }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_logout),
                                    contentDescription = "Settings"
                                )
                            }
                        }

                    }
                )
            }
        ) {
           
            LazyColumn{
                item {
                    ProfileCard(currentUser, userDB)

                }
                item {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))

                        if (args.isNotNull() && updatedUser.isNotNull()){
                            Text(text = "Email",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "${updatedUser.value?.email}")
                            }
                            
//                            Text(text = "Email: ${updatedUser.value?.email}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )

                            Text(text = "Cedula",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "${updatedUser.value?.cedula}")
                            }
//                            Text(text = "Cedula: ${updatedUser.value?.cedula}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )


//                            Text(text = "Telefono: ${updatedUser.value?.phone}",
//
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//
//                            )
                            Text(text = "Telefono",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "0${updatedUser.value?.phone}")
                            }

                        }else{
                            Text(text = "Email",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "${currentUser?.email ?: userDB?.email}")
                            }

//                            Text(text = "Email: ${updatedUser.value?.email}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )

                            Text(text = "Cedula",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "${currentUser?.cedula ?: userDB?.cedula}")
                            }
//                            Text(text = "Cedula: ${updatedUser.value?.cedula}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )


//                            Text(text = "Telefono: ${updatedUser.value?.phone}",
//
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//
//                            )
                            Text(text = "Telefono",
                                style = MaterialTheme.typography.h3,
                                color = Color.LightGray
                            )
                            OutlinedButton(
                                modifier = Modifier.padding(start = 10.dp), onClick = { /*TODO*/ }) {
                                Text(text = "0${currentUser?.phone ?: userDB?.phone}")
                            }
                            
//                            Text(text = "Email: ${userDB?.email ?: currentUser?.email.toString()}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )
//                            Text(text = "Cedula: ${userDB?.cedula ?: currentUser?.cedula.toString()}",
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//                            )
//                            Text(text = "Telefono: ${userDB?.phone ?: currentUser?.phone.toString()}",
//
//                                style = MaterialTheme.typography.h6,
//                                modifier = Modifier.padding(10.dp),
//                                color = Color.DarkGray
//
//                            )

                        }


                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Ubicación",
                            fontSize = 25.sp,
                            )
                       Icon(painterResource(id = R.drawable.ic_location), contentDescription = null)
                    }
                    SmallMap(currentUser, userDB)
                 
                }

            }

        }
    }
}