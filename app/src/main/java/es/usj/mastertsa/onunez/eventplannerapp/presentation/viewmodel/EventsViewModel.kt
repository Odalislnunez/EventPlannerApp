package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import kotlinx.coroutines.flow.Flow

class EventsViewModel(
    private val eventRepository: IEventRepository
) : ViewModel() {

    private val _events = MutableLiveData<Flow<List<Event>>>()
    val event: LiveData<Flow<List<Event>>>
        get() = _events

    suspend fun getEvents() {
        _events.value = eventRepository.getEvents()
    }
}