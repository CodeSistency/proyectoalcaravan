package com.example.proyectoalcaravan.views.charts

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

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
fun BarCharts(
    entries: List<BarEntry?>,
    title: String,
    femaleColor: Int,
    maleColor: Int,
    femaleAverage: Float,
    maleAverage: Float
) {
    val dataSet = BarDataSet(entries, title)

    // Set different colors for female and male bars
    dataSet.colors = listOf(femaleColor, maleColor)

    val data = BarData(dataSet)
    data.setValueTextSize(36f) // Set the font size for the value text

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.text = title
                xAxis.valueFormatter = IndexAxisValueFormatter()
                xAxis.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawLabels(false)
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 100f
                axisRight.isEnabled = false
                legend.isEnabled = true
                setScaleEnabled(false)
                this.data = data

                // Set the values on top of the bars
                dataSet.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }

                // Add labels for women and men
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return if (value == 0f) {
                            "Femenino"
                        } else {
                            "Masculino"
                        }
                    }
                }

                invalidate()
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Femenino: $femaleAverage",
//            color = femaleColor,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Masculino: $maleAverage",
//            color = maleColor,
            style = MaterialTheme.typography.body1
        )
    }
}
@Composable


fun GenderPerformanceChart(viewModel: MainViewModel) {
    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())

    if (userListStudents.isNotEmpty()) {
        val femaleAverage = calculateAveragePerformance(userListStudents, "Femenino")
        val maleAverage = calculateAveragePerformance(userListStudents, "Masculino")

        val entries = listOf(
            BarEntry(0f, femaleAverage),
            BarEntry(1f, maleAverage)
        )

        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
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

private fun calculateAveragePerformance(userListStudents: List<User>, gender: String): Float {
    val filteredUsers = userListStudents.filter { it.gender == gender }
    val totalUsersWithActivities = filteredUsers.count { it.listActivities?.isNotEmpty() == true }

    if (totalUsersWithActivities > 0) {
        val totalPerformance = filteredUsers.sumByDouble { user ->
            val activities = user.listActivities ?: emptyList()
            activities.sumByDouble { it?.calificationRevision?.toDouble() ?: 0.0 }
        }.toFloat()

        return totalPerformance / (totalUsersWithActivities * filteredUsers.flatMap { it.listActivities ?: emptyList() }.size)
    } else {
        return 0F
    }
}

//private fun calculateAveragePerformance(userListStudents: List<User>, gender: String): Float {
//    val filteredUsers = userListStudents.filter { it.gender == gender }
//    val totalPerformance = filteredUsers.fold(0F) { total, user ->
//        val activities = user.listActivities ?: emptyList()
//        val performance = activities.sumOf { it?.calificationRevision?.toDouble() ?: 0.0 }
//        total + performance.toFloat()
//    }
//    return if (filteredUsers.isNotEmpty()) {
//        totalPerformance / (filteredUsers.size * filteredUsers.flatMap { it.listActivities ?: emptyList() }.size)
//    } else {
//        0F
//    }
//}