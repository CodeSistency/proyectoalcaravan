package com.example.proyectoalcaravan.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)

        // Configure the date picker to display in Spanish


        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Format the date in the desired format and set it in the ViewModel
        val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES")).format(
            GregorianCalendar(year, month, day).time
        )
        viewModel.birthday.postValue(formattedDate)
    }
}