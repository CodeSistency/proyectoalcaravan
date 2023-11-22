package com.example.proyectoalcaravan.views.charts

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.mikephil.charting.data.BarEntry

//@Composable
//fun GenderPerformanceChart(viewModel: MainViewModel) {
//
//    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())
//
//    if (userListStudents.isNotEmpty()) {
//        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
//            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//                AndroidView(
//                    factory = { context ->
//                        BarChart(context).apply {
//                            description.text = "Performance Comparison by Gender"
//                            xAxis.valueFormatter = IndexAxisValueFormatter()
//                            xAxis.setDrawGridLines(false)
//                            xAxis.position = XAxis.XAxisPosition.BOTTOM
//                            xAxis.setDrawLabels(false)
//                            axisLeft.axisMinimum = 0f
//                            axisRight.isEnabled = false
//                            legend.isEnabled = true
//                            setScaleEnabled(false)
//
//                            val femaleDataSet = BarDataSet(generateEntryList(userListStudents, "Femenino"), "Masculino")
//                            femaleDataSet.color = Color.BLUE
//
//                            val data = BarData(femaleDataSet)
//                            this.data = data
//                            invalidate()
//                        }
//                    },
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//                AndroidView(
//                    factory = { context ->
//                        BarChart(context).apply {
//                            description.text = "Performance Comparison by Gender"
//                            xAxis.valueFormatter = IndexAxisValueFormatter()
//                            xAxis.setDrawGridLines(false)
//                            xAxis.position = XAxis.XAxisPosition.BOTTOM
//                            xAxis.setDrawLabels(false)
//                            axisLeft.axisMinimum = 0f
//                            axisRight.isEnabled = false
//                            legend.isEnabled = true
//                            setScaleEnabled(false)
//
//                            val maleDataSet = BarDataSet(generateEntryList(userListStudents, "Masculino"), "Femenino")
//                            maleDataSet.color = Color.GREEN
//
//                            val data = BarData(maleDataSet)
//                            this.data = data
//                            invalidate()
//                        }
//                    },
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        }
//    }
//}
//
//
//
//private fun generateEntryList(userListStudents: List<User>, gender: String): List<BarEntry> {
//    return userListStudents.flatMapIndexed { index, user ->
//        val activities = user.listActivities?.filter { it?.idClass == 1 && user.gender == gender }
//        Log.e("gender chart", activities.toString())
//
//        activities!!.mapIndexed { activityIndex, activity ->
//
//            BarEntry((index + 1).toFloat() + (activityIndex.toFloat() / (userListStudents.size + 1)), activity?.calificationRevision?.toFloat() ?: 0F)
//        }
//    }
//}


@Composable


fun GenderPerformanceChartByMateria(viewModel: MainViewModel) {

    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())
    val currentMateriaStudents by viewModel.currentMateria.observeAsState()

    // Filter userListStudents based on common IDs with currentMateriaStudents
    val filteredUserListStudents = userListStudents.filter { user ->

        currentMateriaStudents?.listStudent?.any { it.id == user.id } == true
    }
    fun filterUsers(): List<User> {
        return userListStudents.filter { user ->
            currentMateriaStudents?.listStudent?.any { it.id == user.id } == true
        }
    }

    LaunchedEffect(key1 = true){
        filterUsers()
    }
    Log.e("new filtered List", filterUsers().toString())

    Log.e("new filtered List", filteredUserListStudents.toString())

    if (userListStudents.isNotEmpty()) {
//        val femaleAverage = calculateAveragePerformance(filteredUserListStudents, "Femenino")
//        val maleAverage = calculateAveragePerformance(filteredUserListStudents, "Masculino")

        val femaleAverage =
            currentMateriaStudents?.let {
                calculateAveragePerformance(filterUsers(), "Femenino",
                    it
                )
            }
        val maleAverage =
            currentMateriaStudents?.let {
                calculateAveragePerformance(filterUsers(), "Masculino",
                    it
                )
            }

        val entries = listOf(
            femaleAverage?.let { BarEntry(0f, it) },
            maleAverage?.let { BarEntry(1f, it) }
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)) {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                if (femaleAverage != null && maleAverage != null) {
                    BarCharts(
                        entries,
                        "Rendimiento por generos",
                        Color.MAGENTA,
                        Color.BLUE,
                        femaleAverage,
                        maleAverage
                    )
                }
            }
        }
    }
}


private fun calculateAveragePerformance(userListStudents: List<User>, gender: String, materia: Materia): Float {
    val filteredUsers = userListStudents.filter { it.gender == gender }
    val totalUsersWithActivities = filteredUsers.count { it.listActivities?.isNotEmpty() == true }

    if (totalUsersWithActivities > 0) {
        val totalPerformance = filteredUsers.sumByDouble { user ->
            val activities = user.listActivities?.filter { it?.idClass == materia.id } ?: emptyList()

//            val activities = user.listActivities ?: emptyList()
            activities.sumByDouble { it?.calificationRevision?.toDouble() ?: 0.0 }
        }.toFloat()

        return totalPerformance / (totalUsersWithActivities * filteredUsers.flatMap { it.listActivities ?: emptyList() }.size)
    } else {
        return 0F
    }
}