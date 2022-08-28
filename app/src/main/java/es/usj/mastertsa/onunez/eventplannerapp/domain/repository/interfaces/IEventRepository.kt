package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IEventRepository {

    suspend fun getAllUserEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getUncomingUserEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getPastUserEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getAllPublicEvents(): Flow<DataState<List<Event>>>

    suspend fun saveEvent(event: Event): Flow<DataState<Boolean>>
}