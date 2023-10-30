package com.example.proyectoalcaravan.views.profesor

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            OutlinedTextField(
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
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
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filters),
                    contentDescription = "Filter",
                    tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

    }


    @Composable
    fun ListItem(item: User) {

        var isModalVisible by remember { mutableStateOf(false) }


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
//                CoilImage(
//                    data = user.imageUrl,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape)
//                )
                Image(imageVector = Icons.Default.AccountCircle,
                    contentDescription = "user",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // User information
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.firstName.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = item.lastName.toString(),
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                }

                // Delete button
                IconButton(
                    onClick = { isModalVisible = true },
                    modifier = Modifier
                        .size(38.dp)
//                        .background(Color.Red, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }

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
                            Column(
                                modifier = Modifier.background(Color.White)
                                    .width(500.dp)
                                    .height(300.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Estas seguro que deseas eliminar este usuario")
                                Button(onClick = {  },

                                    ) {
                                    Text(text = "Eliminar")
                                }
                                Button(onClick = { isModalVisible = false }) {
                                    Text(text = "Cancelar")

                                }
                            }
                        }
                    }
                )
            }

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
                onClick = { view?.findNavController()?.navigate(R.id.action_profesorFragment_to_materiaFragment) },
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
        var skipHalfExpanded by remember { mutableStateOf(false) }
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
                        val iconPainter: Painter = painterResource(R.drawable.qr_scan_svgrepo_com)
                        Icon(
                            painter = iconPainter,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(40.dp)
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
                                QrCodeScanner()


                                Button(onClick = { isModalVisible = false }) {
                                        Text(text = "Cancelar")


                                }
                            }
                        }
                    )
                }

                ModalBottomSheetLayout(sheetState = state,

                    sheetContent ={


                        QrCodeScanner()

                }) {

                }
            }
        }


    }


}