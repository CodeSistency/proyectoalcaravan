package com.example.proyectoalcaravan.views.student

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
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

import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R

import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
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
//                MyFragmentContent(viewModel)
//                Button(onClick = { checkCamaraPermissions() }) {
//                    Text(text = "Scanner")
//                }
//
//                Text(text = "${textResult}")
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
        Text(text = "Your Title", style = MaterialTheme.typography.h5)
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
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = "Filter",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }


    @Composable
    fun ListItem(item: String) {
        // Replace with your list item implementation
        // You can use Card, ListItem, or any other Composable to represent a single item in the list
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            elevation = 4.dp
        ) {
            Text(text = item, modifier = Modifier.padding(16.dp))
        }
    }

    @Composable
    fun ListContent() {
        // Replace with your list content
        LazyColumn {
            items(40) { index ->
                ListItem(item = "Item $index")
            }
        }
    }

    @Composable
    fun BottomAppBarContent() {
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
            BottomNavigationItem(
                selected = false,
                onClick = { /* Handle bottom navigation item click */ },
                icon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Tarea")
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {
                    view?.findNavController()?.navigate(R.id.action_studentFragment_to_profileFragment)
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

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Optional padding
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Title()
                    IconButton(onClick = { scope.launch { state.show() }}) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "QR Code"
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
                SearchBar()
                ListContent()

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent ={


                        if (BarcodeType.QR_CODE.isValueValid(viewModel.currentUser.value?.cedula.toString())) {
                            Barcode(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                    .fillMaxSize().padding(10.dp, 10.dp, 10.dp, 40.dp),
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