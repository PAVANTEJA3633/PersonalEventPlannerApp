package com.example.projecteventplanner

import androidx.lifecycle.LiveData

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    suspend fun insert(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun update(event: Event) {
        eventDao.updateEvent(event)
    }

    suspend fun delete(event: Event) {
        eventDao.deleteEvent(event)
    }
}