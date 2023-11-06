package com.example.proyectoalcaravan.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoalcaravan.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class MpaChartsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mpa_charts, container, false)

        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        // Create data entries
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 20f))
        entries.add(Entry(3f, 15f))
        entries.add(Entry(4f, 30f))
        entries.add(Entry(5f, 25f))

        val dataSet = LineDataSet(entries, "Sample Data")
        val lineData = LineData(dataSet)

        lineChart.data = lineData

        return view
    }
}