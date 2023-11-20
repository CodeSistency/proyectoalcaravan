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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        )

        // Observe the viewLifecycleOwnerLiveData to get the viewLifecycleOwner
        viewLifecycleOwnerLiveData.observe(this) { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    // Remove the observer when the Fragment is destroyed
                    viewModel.birthday.removeObservers(this@DatePickerFragment)
                }
            })

            // Set up an observer for the birthday property in the ViewModel
            viewModel.birthday.observe(viewLifecycleOwner) { newFormattedDate ->
                // Parse the new formatted date to get year, month, and day
                val defaultDate = parseFormattedDate(newFormattedDate)

                // Update the DatePickerDialog with the new default date
                datePickerDialog.updateDate(
                    defaultDate.first,
                    defaultDate.second,
                    defaultDate.third
                )
            }
        }

        // Configure the date picker to display in Spanish

        return datePickerDialog
    }

    private fun parseFormattedDate(formattedDate: String): Triple<Int, Int, Int> {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES"))
        val date = dateFormat.parse(formattedDate)
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()

        return Triple(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES")).format(
            GregorianCalendar(year, month, day).time
        )
        viewModel.birthday.postValue(formattedDate)
    }
}

//class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
//
//    private val viewModel by activityViewModels<MainViewModel>()
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        // Use the current date as the default date in the picker.
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//        // Create a new instance of DatePickerDialog and return it.
//        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
//
//        // Configure the date picker to display in Spanish
//
//
//        return datePickerDialog
//    }
//
//    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//        // Format the date in the desired format and set it in the ViewModel
//        val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES")).format(
//            GregorianCalendar(year, month, day).time
//        )
//
//        viewModel.birthday.postValue(formattedDate)
//    }
//}