package com.example.proyectoalcaravan.views

import android.annotation.SuppressLint
//import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
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
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType


class ProfileFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

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
    fun ProfileCard(currentUser: User?) {
        Column {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Color.Blue.copy(alpha = 0.8F),
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
                    // You can use CoilImage or Image with your image source
//                    CoilImage(
//                        data = "Your Image URL or Resource",
//                        contentDescription = "Profile Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
                    GlideImage(
                        model = currentUser?.imageProfile,
                        contentDescription = "foto",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Fit                   )

//                    Image(imageVector = Icons.Default.AccountCircle,
//                        contentDescription = "user",
//
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        contentScale = ContentScale.Crop
//
//                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                        text = currentUser?.firstName.toString(),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = currentUser?.lastName.toString(),
                        style = MaterialTheme.typography.body1,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                // Other Composables (Name, Bio, etc.)
                // Add your other composables here

                Spacer(modifier = Modifier.weight(1f))
            }
        }
        }
    }

    @Composable
    fun SmallMap(currentUser: User?) {
        val context = LocalContext.current

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
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(currentUser?.lat ?: 0.078867, currentUser?.lgn ?: 0.078867)) // Replace with your desired location
                    .zoom(15f) // Zoom level
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun Profile(currentUser: User?) {
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
                        IconButton(onClick = {view?.findNavController()?.navigate(ProfileFragmentDirections.actionProfileFragmentToRegisterStepTwo2(true,currentUser?.id?: 1000)) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_editar),
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            }
        ) {
            LazyColumn{
                item {
                    ProfileCard(currentUser)

                }
                item {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "Email: ${currentUser?.email.toString()}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(10.dp),
                            color = Color.DarkGray
                        )



                        Text(text = "Cedula: ${currentUser?.cedula.toString()}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(10.dp),
                            color = Color.DarkGray
                        )


                        Text(text = "Telefono: ${currentUser?.phone.toString()}",

                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(10.dp),
                            color = Color.DarkGray

                        )

                    }
                    SmallMap(currentUser)
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        lineChartData = lineChartData
                    )
                }
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
////                verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // User Photo
//                    Image(
//                        painter = painterResource(R.drawable.ic_profile),
//                        contentDescription = "User Photo",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(vertical = 16.dp),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    // User Information
//                    Text(
//                        text = currentUser?.firstName.toString(),
//                        style = MaterialTheme.typography.h6,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//                    Text(
//                        text = currentUser?.lastName.toString(),
//                        style = MaterialTheme.typography.body1,
//                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
//                    )
//            }


//                if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.cedula.toString())) {
//                    Barcode(
//                        modifier = Modifier
//                            .align(Alignment.CenterHorizontally)
//                            .fillMaxSize()
//                            .padding(10.dp, 10.dp, 10.dp, 40.dp),
//                        resolutionFactor = 10, // Optionally, increase the resolution of the generated image
//                        type = BarcodeType.QR_CODE, // pick the type of barcode you want to render
//                        value = viewModel.currentUser.value?.cedula.toString() // The textual representation of this code
//                    )
//                }

            }



        }
    }
}