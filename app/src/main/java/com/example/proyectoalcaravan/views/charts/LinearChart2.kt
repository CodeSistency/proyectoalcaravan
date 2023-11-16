package com.example.proyectoalcaravan.views.charts

import android.graphics.Color
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.activityViewModels
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChart2(viewModel: MainViewModel) {
    val currentUser by viewModel.updatedUser.observeAsState()

    currentUser?.let { user ->
        val activities = user.listActivities?.filter { it?.idClass == viewModel.currentMateria.value?.id }
        val entries = activities?.mapIndexed { index, activity ->
            activity?.calificationRevision?.let { Entry((index + 1).toFloat(), it.toFloat()) }
        }

        val dataSet = LineDataSet(entries, "Calificaciones")
        dataSet.color = Color.BLUE
        dataSet.circleColors = listOf(Color.BLUE)
        dataSet.valueTextSize = 12f
        dataSet.setDrawValues(true)

        Box(modifier = Modifier.fillMaxWidth().height(500.dp)) {
            AndroidView(
                factory = { context ->
                    LineChart(context).apply {
                        data = LineData(dataSet)
                        description.text = "Calificaciones"
                        axisLeft.axisMinimum = 0f
                        axisLeft.axisMaximum = 100f
                        invalidate()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}