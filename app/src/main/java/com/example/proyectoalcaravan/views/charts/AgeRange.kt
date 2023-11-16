package com.example.proyectoalcaravan.views.charts

import android.content.Context
import android.graphics.Color
//import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

//@Composable
//fun AgeRangePerformanceChart(viewModel: MainViewModel, context: Context) {
//    viewModel.getUserStudents("Estudiante", context)
//    val userListStudents by viewModel.userStudentsList.observeAsState()
//
//    if (userListStudents?.isNotEmpty() == true) {
//        val ageRanges = listOf("18-25", "26-35", "36-45", "46-55", "56+")
//        Log.e("is not emppty", "algo")
//        val performanceByAgeRange =
//            userListStudents?.let { calculatePerformanceByAgeRange(it, ageRanges) }
//
//        val entries = performanceByAgeRange?.mapIndexed { index, performance ->
//            BarEntry(index.toFloat(), performance)
//        }
//
//        // Print debug information
//        performanceByAgeRange?.forEachIndexed { index, value ->
//            Log.e("Age Range", "Age Range: ${ageRanges[index]}, Performance: $value")
//            println("Age Range: ${ageRanges[index]}, Performance: $value")
//        }
//
//        ageRanges.forEachIndexed { index, range ->
//            println("Index: $index, Age Range: $range")
//            Log.e("index", "Index: $index, Age Range: $range")
//
//        }
//
//        entries?.forEachIndexed { index, entry ->
//            Log.e("Entry", "Entry $index: X = ${entry.x}, Y = ${entry.y}")
//            println("Entry $index: X = ${entry.x}, Y = ${entry.y}")
//        }
//
//        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
//            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//                if (entries != null) {
//                    BarCharts(
//                        entries,
//                        "Performance by Age Range",
//                        Color.GREEN,
//                        Color.YELLOW,
//                        performanceByAgeRange.maxOrNull() ?: 0f,
//                        performanceByAgeRange.maxOrNull() ?: 0f
//                    )
//                }
//            }
//        }
//    }
//}
//
////private fun calculatePerformanceByAgeRange(userListStudents: List<User>, ageRanges: List<String>): List<Float> {
////    val performanceByAgeRange = MutableList(ageRanges.size) { 0f }
////
////    userListStudents.forEach { user ->
////        val age = user.edad ?: return@forEach
////        val performance = user.listActivities?.sumOf { it?.calificationRevision?.toDouble() ?: 0.0 }?.toFloat() ?: 0f
////
////        val index = getAgeRangeIndex(age, ageRanges)
////        if (index != -1) {
////            performanceByAgeRange[index] += performance
////        }
////    }
////
////    return performanceByAgeRange
////}
//private fun calculatePerformanceByAgeRange(userListStudents: List<User>, ageRanges: List<String>): List<Float> {
//    Log.e("userList age range", userListStudents.toString())
//    var performanceByAgeRange = MutableList(ageRanges.size) { 0f }
//
//
//        Log.e("UserListSize", "Size of userListStudents: ${userListStudents.size}")
//        Log.e("AgeRange", "Debugging: Before forEach loop")
//
//        userListStudents.forEach { user ->
//            val age = user.edad ?: return@forEach
//            val totalActivities = user.listActivities?.size ?: 0
//
//            if (totalActivities > 0) {
//                // Calculate the average calificationRevision for each user
//                Log.e("AgeRange", "Debugging: Inside forEach loop - Checking age: $age, totalActivities: $totalActivities")
//
//                val averagePerformance = user.listActivities
//                    ?.mapNotNull { it?.calificationRevision?.toDouble() }
//                    ?.average()
//                    ?.toFloat() ?: 0f
//
//                val index = getAgeRangeIndex(age, ageRanges)
//
//                if (index != -1) {
//                    // Add the average performance to the corresponding age range
//                    val performanceToAdd = if (averagePerformance.isNaN()) {
//                        0f
//                    } else {
//                        averagePerformance / totalActivities
//                    }
//
//                    Log.e("AgeRange", "Age: $age, Total Activities: $totalActivities, Average Performance: $averagePerformance, Performance to Add: $performanceToAdd")
//                    performanceByAgeRange[index] += performanceToAdd
//                }
//                Log.e("AgeRange", "Debugging: Inside forEach loop - User $age processed")
//            }
//        }
//
//        Log.e("AgeRange", "Debugging: Outside forEach loop")
//
//
//    return performanceByAgeRange
//
//}
//
//
//private fun getAgeRangeIndex(age: Int, ageRanges: List<String>): Int {
//    for (i in ageRanges.indices) {
//        val range = ageRanges[i]
//        val (start, end) = range.split("-")
//        val startAge = start.toInt()
//        val endAge = end.toInt()
//
//        if (age in startAge..endAge) {
//            return i
//        }
//    }
//    return -1
//}

//@Composable
//fun BarChartView(agePerformanceMap: Map<Int, Float>) {
//    BarChart(
//        modifier = Modifier.fillMaxSize(),
//        data = BarData(
//            BarDataSet(
//                agePerformanceMap.map { (ageRange, performance) ->
//                    BarEntry(ageRange.toFloat(), performance)
//                },
//                "Performance"
//            )
//        ),
//    ).apply {
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.labelCount = agePerformanceMap.size
//        xAxis.valueFormatter = AgeRangeAxisFormatter()
//        description = null
//    }
//}
//
//private fun calculateAveragePerformanceByAge(users: List<User>): Map<Int, Float> {
//    val agePerformanceMap = mutableMapOf<Int, Float>()
//
//    users.forEach { user ->
//        val age = user.edad ?: return@forEach
//        val totalCalificationRevision = user.listActivities
//            ?.filterNotNull()
//            ?.sumBy { it.calificationRevision ?: 0 } ?: 0
//
//        if (agePerformanceMap.containsKey(age)) {
//            val currentPerformance = agePerformanceMap[age] ?: 0f
//            agePerformanceMap[age] = currentPerformance + totalCalificationRevision.toFloat()
//        } else {
//            agePerformanceMap[age] = totalCalificationRevision.toFloat()
//        }
//    }
//
//    agePerformanceMap.forEach { (age, totalPerformance) ->
//        val userCount = users.count { it.edad == age }
//        val averagePerformance = totalPerformance / userCount
//        agePerformanceMap[age] = averagePerformance
//    }
//
//    return agePerformanceMap
//}

//@Composable
//fun AgeRangePerformanceChart(viewModel: MainViewModel, context: Context) {
//    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())
//    viewModel.getUserStudents("Estudiante", context)
//
//    if (userListStudents.isNotEmpty()) {
//        val ageRanges = listOf("18-25", "26-35", "36-45", "46-55", "56+")
//        val performanceByAgeRange = calculatePerformanceByAgeRange(userListStudents, ageRanges)
//
//        val entries = performanceByAgeRange.mapIndexed { index, performance ->
//            BarEntry(index.toFloat(), performance)
//        }
//
//        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
//            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//                BarCharts(
//                    entries,
//                    "Performance by Age Range",
//                    Color.GREEN,
//                    Color.YELLOW,
//                    performanceByAgeRange.maxOrNull() ?: 0f,
//                    performanceByAgeRange.maxOrNull() ?: 0f
//                )
//            }
//        }
//    }
//}
//
//private fun calculatePerformanceByAgeRange(userListStudents: List<User>, ageRanges: List<String>): List<Float> {
//    val performanceByAgeRange = MutableList(ageRanges.size) { 0f }
//
//    userListStudents.forEach { user ->
//        val age = user.edad ?: return@forEach
//        val performance = user.listActivities?.sumOf { it?.calificationRevision?.toDouble() ?: 0.0 }?.toFloat() ?: 0f
//
//        val index = getAgeRangeIndex(age, ageRanges)
//        if (index != -1) {
//            performanceByAgeRange[index] += performance
//        }
//    }
//
//    return performanceByAgeRange
//}
//
//private fun getAgeRangeIndex(age: Int, ageRanges: List<String>): Int {
//    for (i in ageRanges.indices) {
//        val range = ageRanges[i]
//        val (start, end) = range.split("-")
//        val startAge = start.toInt()
//        val endAge = end.toInt()
//
//        if (age in startAge..endAge) {
//            return i
//        }
//    }
//    return -1
//}

@Composable
fun AgeRangePerformanceChart(viewModel: MainViewModel, context: Context) {
    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())
    viewModel.getUserStudents("Estudiante", context)

    if (userListStudents.isNotEmpty()) {
        val ageRanges = listOf("18-25", "26-35", "36-45", "46-55", "56+")
        val performanceByAgeRange = calculatePerformanceByAgeRange(userListStudents, ageRanges)
        Log.e("Performance", performanceByAgeRange.toString())

        val barEntries = performanceByAgeRange.mapIndexed { index, performance ->
            BarEntry(index.toFloat(), performance)
        }

        val barDataSet = BarDataSet(barEntries, "Rendimiento por rango de edad").apply {
            setColors(Color.GREEN)
            setDrawValues(true)
        }

        val barData = BarData(barDataSet)

        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = barData,
                labels = ageRanges
            )
        }
    }
}

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    data: BarData,
    labels: List<String>
) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)

                data.barWidth = 0.4f

                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = 1000f
                }

                axisRight.isEnabled = false

                setData(data)
                invalidate()
            }
        },
        modifier = modifier
    )
}

private fun calculatePerformanceByAgeRange(userListStudents: List<User>, ageRanges: List<String>): List<Float> {
    val performanceByAgeRange = MutableList(ageRanges.size) { 0f }

    userListStudents.forEach { user ->
        val age = user.edad ?: return@forEach
        val totalActivities = user.listActivities?.size ?: 0

        if (totalActivities > 0) {
            // Calculate the average calificationRevision for each user, based on a maximum of 100
            val averagePerformance = (user.listActivities
                ?.mapNotNull { it?.calificationRevision?.toDouble() }
                ?.average()
                ?.toFloat() ?: 0f) * 100 / totalActivities

            val index = getAgeRangeIndex(age, ageRanges)

            if (index != -1) {
                // Add the average performance to the corresponding age range, capped at 100
                performanceByAgeRange[index] += averagePerformance
            }
        }
    }

    return performanceByAgeRange
}

private fun getAgeRangeIndex(age: Int, ageRanges: List<String>): Int {
    for (i in ageRanges.indices) {
        val range = ageRanges[i]
        val (start, end) = range.split("-")
        val startAge = start.toInt()
        val endAge = end.toInt()

        if (age in startAge..endAge) {
            return i
        }
    }
    return -1
}



