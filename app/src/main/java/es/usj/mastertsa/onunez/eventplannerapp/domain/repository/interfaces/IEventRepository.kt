package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    suspend fun getEvents(user: User): Flow<DataState<List<Event>>>?

    suspend fun getPublicEvents(): Flow<DataState<List<Event>>>?

    suspend fun addEvent(event: Event)

    suspend fun updateEvent(event: Event)
}