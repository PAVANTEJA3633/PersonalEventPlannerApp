package com.example.projecteventplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventplanner.databinding.ItemEventBinding

class EventAdapter(
    private val onDeleteClick: (Event) -> Unit,
    private val onEditClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var eventList = emptyList<Event>()

    class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = eventList[position]

        holder.binding.tvTitle.text = currentEvent.title
        holder.binding.tvCategory.text = "Category: ${currentEvent.category}"
        holder.binding.tvLocation.text = "Location: ${currentEvent.location}"
        holder.binding.tvDateTime.text = "Date: ${currentEvent.date}   Time: ${currentEvent.time}"

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(currentEvent)
        }

        holder.binding.btnEdit.setOnClickListener {
            onEditClick(currentEvent)
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun setData(events: List<Event>) {
        eventList = events
        notifyDataSetChanged()
    }
}