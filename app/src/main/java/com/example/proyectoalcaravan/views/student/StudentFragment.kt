package com.example.proyectoalcaravan.views.student

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Materia

import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.DatePickerFragment
import com.example.proyectoalcaravan.views.MpaChartsFragment
import com.example.proyectoalcaravan.views.charts.AgeRangePerformanceChart
import com.example.proyectoalcaravan.views.charts.GenderPerformanceChart
import com.example.proyectoalcaravan.views.charts.LineChart
import com.example.proyectoalcaravan.views.qrScanner.QrCodeScanner
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
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
    fun Title() {
        // Replace "Your Title" with your actual title string
        Text(text = "Home", style = MaterialTheme.typography.h5)
    }

    @Composable
    fun SearchBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(12.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                value = "",
                onValueChange = { /* Handle search bar value change */ },
                placeholder = {
                    Text(text = "Buscar", style = MaterialTheme.typography.body1)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            IconButton(
                onClick = { /* Handle filter icon button click */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filters),
                    contentDescription = "Filter",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }



    @Composable
    fun ListItem(item: Materia) {

        var user = viewModel.currentUser.value

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clickable {
                    viewModel.getActivitiesById(item.id)
//                    viewModel.getMateriaById(item.id)
//                    viewModel.currentMateria.postValue(item)
                    if (viewModel.currentUser.value?.rol == "Estudiante") {

                        view
                            ?.findNavController()
                            ?.navigate(
                                StudentFragmentDirections.actionStudentFragmentToAsignacionFragment(
                                    user?.id ?: 1000
                                )
                            )
                    } else {
                        viewModel.getMateriaById(item.id)
                        viewModel.currentMateria.postValue(item)
                        view
                            ?.findNavController()
                            ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
                    }

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


        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Optional padding
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Title()
                    IconButton(onClick = { isModalVisible = true}) {

//                    IconButton(onClick = { scope.launch { state.show() }}) {
                        val iconPainter: Painter = painterResource(R.drawable.qr_detailed_svgrepo_com)
                        Icon(

                            painter = iconPainter,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(20.dp)

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
//                SearchBar()
                ListContent()

//                LineChart(viewModel = viewModel)
//                GenderPerformanceChart(viewModel = viewModel)
                AgeRangePerformanceChart(viewModel = viewModel)

//                AndroidView(
//                    factory = { context ->
//                        val fragment = MpaChartsFragment()
//                        val inflater = LayoutInflater.from(context)
//                        fragment.onCreateView(inflater, null, null)
//                    }
//                )


//                newFragment.show(childFragmentManager, "datePicker")

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
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = 4.dp
                                ) {

                                        if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.cedula.toString())) {
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
//                                        Button(onClick = { isModalVisible = false }) {
//                                            Text(text = "Cancelar")
//
//                                        }

                                }

                            }
                        }
                    )
                }

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent ={


                        if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.cedula.toString())) {
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