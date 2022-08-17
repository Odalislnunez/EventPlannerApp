package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    suspend fun getEvents(user: User): Flow<List<Event>>?

    suspend fun getPublicEvents(): Flow<List<Event>>?

    suspend fun addEvent(event: Event)

    suspend fun updateEvent(event: Event)
}