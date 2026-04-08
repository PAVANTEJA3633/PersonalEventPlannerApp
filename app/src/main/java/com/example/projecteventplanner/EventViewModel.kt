package com.example.projecteventplanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>

    init {
        val eventDao = EventDatabase.getDatabase(application).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.allEvents
    }

    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch {
        repository.update(event)
    }

    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }
}