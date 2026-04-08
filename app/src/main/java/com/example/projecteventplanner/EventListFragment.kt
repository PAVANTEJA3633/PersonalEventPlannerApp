package com.example.projecteventplanner

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projecteventplanner.databinding.FragmentEventListBinding
import com.google.android.material.snackbar.Snackbar

class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventViewModel: EventViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventViewModel = ViewModelProvider(this)[EventViewModel::class.java]

        adapter = EventAdapter(
            onDeleteClick = { event ->
                eventViewModel.delete(event)
                Snackbar.make(binding.root, "Event deleted", Snackbar.LENGTH_SHORT).show()
            },
            onEditClick = { event ->
                showEditDialog(event)
            }
        )

        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = adapter

        eventViewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.setData(events)

            if (events.isEmpty()) {
                Toast.makeText(requireContext(), "No events available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDialog(event: Event) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_event, null)

        val etEditTitle = dialogView.findViewById<EditText>(R.id.etEditTitle)
        val etEditCategory = dialogView.findViewById<EditText>(R.id.etEditCategory)
        val etEditLocation = dialogView.findViewById<EditText>(R.id.etEditLocation)
        val etEditDate = dialogView.findViewById<EditText>(R.id.etEditDate)
        val etEditTime = dialogView.findViewById<EditText>(R.id.etEditTime)

        etEditTitle.setText(event.title)
        etEditCategory.setText(event.category)
        etEditLocation.setText(event.location)
        etEditDate.setText(event.date)
        etEditTime.setText(event.time)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Event")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = etEditTitle.text.toString().trim()
                val updatedCategory = etEditCategory.text.toString().trim()
                val updatedLocation = etEditLocation.text.toString().trim()
                val updatedDate = etEditDate.text.toString().trim()
                val updatedTime = etEditTime.text.toString().trim()

                if (updatedTitle.isEmpty() || updatedDate.isEmpty()) {
                    Toast.makeText(requireContext(), "Title and Date cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updatedEvent = event.copy(
                    title = updatedTitle,
                    category = updatedCategory,
                    location = updatedLocation,
                    date = updatedDate,
                    time = updatedTime
                )

                eventViewModel.update(updatedEvent)
                Snackbar.make(binding.root, "Event updated", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}