package com.example.proyectoalcaravan.views.profesor

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.qrScanner.QrCodeScanner
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import kotlinx.coroutines.launch

//import kotlinx.coroutines.launch


class ProfesorFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

//    val qrCode = viewModel.currentUser.value?.cedula
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
                ProfesorContent()
//                MyFragmentContent(viewModel)
//                Button(onClick = { checkCamaraPermissions() }) {
//                    Text(text = "Scanner")
//                }
//
//                Text(text = "${textResult}")
            }
        }
    }


    @Composable
    fun Title() {
        // Replace "Your Title" with your actual title string
        Text(text = "Your Title", style = MaterialTheme.typography.h5)
    }


    @Composable
    fun SearchBar() {
        Row {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
//                    .padding(end = 8.dp),
                    .padding(12.dp),

                value = "",
                onValueChange = { /* Handle search bar value change */ },
                placeholder = {
                    Text(text = "Buscar", style = MaterialTheme.typography.body1)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
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
    fun ListItem(item: User) {
        // Replace with your list item implementation
        // You can use Card, ListItem, or any other Composable to represent a single item in the list
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            elevation = 4.dp
        ) {
            Text(text = item.firstName.toString(), modifier = Modifier.padding(16.dp))
//            Text(text = item.firstName, modifier = Modifier.padding(16.dp))

        }
    }

    @Composable
    fun ListContent(userList: MutableLiveData<List<User>>) {
        val users = userList.value ?: emptyList()

        LazyColumn {
            items(users) { user ->
                ListItem(item = user)
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
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Actividades")
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = {view?.findNavController()?.navigate(R.id.action_profesorFragment_to_profileFragment)
                },
                icon = {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Usuario")
                }
            )
        }
    }

    @Composable
    fun BottomSheetContent() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(10) { index ->
                Text(text = "Item $index")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ProfesorContent() {
//        var isBottomSheetOpen by remember { mutableStateOf(false) }
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
                        val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
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
                SearchBar()

                ListContent(userList = viewModel.userList)

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent ={


                        QrCodeScanner()

                }) {

                }
            }
        }


    }


}