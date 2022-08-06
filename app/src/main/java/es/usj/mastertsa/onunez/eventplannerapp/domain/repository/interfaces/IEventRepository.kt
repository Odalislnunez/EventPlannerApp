package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    suspend fun getEvents(): Flow<List<Event>>?

    suspend fun addEvents(event: Event)
}