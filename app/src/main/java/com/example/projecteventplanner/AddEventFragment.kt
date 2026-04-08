package com.example.projecteventplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projecteventplanner.databinding.FragmentAddEventBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEventFragment : Fragment() {

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventViewModel: EventViewModel
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventViewModel = ViewModelProvider(this)[EventViewModel::class.java]

        setupCategorySpinner()
        setupDatePicker()
        setupTimePicker()
        setupSaveButton()
    }

    private fun setupCategorySpinner() {
        val categories = listOf("Work", "Social", "Travel", "Study", "Personal")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val today = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedCalendar.set(Calendar.YEAR, year)
                    selectedCalendar.set(Calendar.MONTH, month)
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.etDate.setText(dateFormat.format(selectedCalendar.time))
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = today.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            val currentHour = selectedCalendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = selectedCalendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)

                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    binding.etTime.setText(timeFormat.format(selectedCalendar.time))
                },
                currentHour,
                currentMinute,
                false
            )
            timePickerDialog.show()
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveEvent.setOnClickListener {
            saveEvent()
        }
    }

    private fun saveEvent() {
        val title = binding.etTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val location = binding.etLocation.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val time = binding.etTime.text.toString().trim()

        if (title.isEmpty()) {
            binding.etTitle.error = "Title is required"
            binding.etTitle.requestFocus()
            return
        }

        if (date.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
            return
        }

        if (time.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show()
            return
        }

        val now = Calendar.getInstance()
        if (selectedCalendar.before(now)) {
            Toast.makeText(requireContext(), "Past date/time is not allowed", Toast.LENGTH_SHORT).show()
            return
        }

        val event = Event(
            title = title,
            category = category,
            location = location,
            date = date,
            time = time
        )

        eventViewModel.insert(event)

        Snackbar.make(binding.root, "Event saved successfully", Snackbar.LENGTH_SHORT).show()

        clearFields()
    }

    private fun clearFields() {
        binding.etTitle.text?.clear()
        binding.etLocation.text?.clear()
        binding.etDate.text?.clear()
        binding.etTime.text?.clear()
        binding.spinnerCategory.setSelection(0)
        selectedCalendar = Calendar.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}