package com.example.proyectoalcaravan.views.charts

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectoalcaravan.model.remote.User
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.mikephil.charting.data.BarEntry

@Composable
fun AgeRangePerformanceChart(viewModel: MainViewModel) {
    val userListStudents by viewModel.userStudentsList.observeAsState(emptyList())

    if (userListStudents.isNotEmpty()) {
        val ageRanges = listOf("18-25", "26-35", "36-45", "46-55", "56+")
        val performanceByAgeRange = calculatePerformanceByAgeRange(userListStudents, ageRanges)

        val entries = performanceByAgeRange.mapIndexed { index, performance ->
            BarEntry(index.toFloat(), performance)
        }

        Column(modifier = Modifier.fillMaxWidth().height(500.dp)) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                BarCharts(
                    entries,
                    "Performance by Age Range",
                    Color.GREEN,
                    Color.YELLOW,
                    performanceByAgeRange.maxOrNull() ?: 0f,
                    performanceByAgeRange.maxOrNull() ?: 0f
                )
            }
        }
    }
}

private fun calculatePerformanceByAgeRange(userListStudents: List<User>, ageRanges: List<String>): List<Float> {
    val performanceByAgeRange = MutableList(ageRanges.size) { 0f }

    userListStudents.forEach { user ->
        val age = user.edad ?: return@forEach
        val performance = user.listActivities?.sumOf { it?.calificationRevision?.toDouble() ?: 0.0 }?.toFloat() ?: 0f

        val index = getAgeRangeIndex(age, ageRanges)
        if (index != -1) {
            performanceByAgeRange[index] += performance
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