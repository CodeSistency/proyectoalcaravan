package com.example.proyectoalcaravan.views

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.utils.generateRandomColor
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.example.proyectoalcaravan.views.componentes.ColorList
import com.example.proyectoalcaravan.views.componentes.Header
import com.example.proyectoalcaravan.views.componentes.shimmer.ShimmerCardList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ClasesFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentLifecycle", "Fragment created: ${javaClass.simpleName}")
        viewModel.getAllMaterias(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ClasesContent()

                transitionName = "sharedElement"
            }
        }
    }

@Composable
fun ListItem(item: Materia) {
    // Replace with your list item implementation
    // You can use Card, ListItem, or any other Composable to represent a single item in the list
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable(

            ) {
                val extras = FragmentNavigatorExtras(
                    requireView().findViewById<View>(R.id.compose_materia) to "sharedElement"
                )

                viewModel.currentMateria.postValue(item)
                viewModel.getActivitiesById(item.id, requireContext())
                view
                    ?.findNavController()
                    ?.navigate(R.id.action_clasesFragment_to_materiaFragment, null, null, extras)
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
                Text(text = "Inscritos: ${item.listStudent?.size}",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 2.dp),
                    fontSize = 15.sp,
                    color = Color.DarkGray
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
    fun ListContent(materiasList: MutableLiveData<List<Materia>>) {
        val materias = materiasList.value ?: emptyList()

        LazyColumn {
            items(materias) { materia ->
                ListItem(item = materia)
            }
        }
    }

    @Composable
    fun GridItemCard(item: Materia) {
        var isClickable by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()
        val backgroundColor = ColorList.colors[item.id % ColorList.colors.size]


        Box(
            modifier = Modifier
                .padding(4.dp)
                .height(130.dp)
//                .size(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
//                .border(2.dp, colorResource(id = R.color.blue_dark), RoundedCornerShape(16.dp))
//                .background(generateRandomColor())
//                .background(
//                    brush = Brush.linearGradient(
//                        colors = listOf(Color.Black, Color.Transparent),
//                        start = Offset(0.5f, 1.0f),
//                        end = Offset(0.5f, 0.5f)
//                    )
//                )
                .clickable {
                    if(isClickable){
                        isClickable = false

                        // Launch a coroutine to re-enable clickable after a delay
                        coroutineScope.launch {
                            delay(2000) // Adjust the delay duration as needed (in milliseconds)
                            isClickable = true
                        }
                        viewModel.currentMateria.postValue(item)
//                    viewModel.getActivitiesById(item.id, requireContext())
                        view
                            ?.findNavController()
                            ?.navigate(R.id.action_clasesFragment_to_materiaFragment)
                    }

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterStart),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f), // Adjust alpha for transparency
                modifier = Modifier
                    .align(Alignment.TopEnd)
//                    .absolutePadding(top = (-30).dp, right = (-30).dp)
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Grid(gridItems: MutableLiveData<List<Materia>>) {
        val items by gridItems.observeAsState(initial = emptyList())

        var refresh = viewModel.refreshingMaterias.observeAsState()


        val pullRefreshState = rememberPullRefreshState(refreshing = refresh.value ?: false, { viewModel.getAllMaterias(requireContext()) })

        if (refresh.value == true){
            ShimmerCardList()
        }else{
            Box(Modifier.pullRefresh(pullRefreshState)){
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(items) { item ->
                        GridItemCard(item)
                    }
                }
                PullRefreshIndicator(refreshing = refresh.value?: false, pullRefreshState, Modifier.align(Alignment.TopCenter))

            }
        }


    }


    @Composable
    fun ClasesContent() {
        Column {
            Header(titulo = "Materias")
            Grid(gridItems = viewModel.materiasList)
        }

    }
}