package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IEventRepository {

    suspend fun getAllEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getUncomingEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getPastEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getAllPublicEvents(): Flow<DataState<List<Event>>>

    suspend fun saveEvent(event: Event): Flow<DataState<Boolean>>

    suspend fun getEventCreators(creators: List<String>): Flow<DataState<List<User>>>

    suspend fun getEventParticipants(participants: List<String>): Flow<DataState<List<User>>>

    suspend fun participateEvent(userId: String, eventId: String): Flow<DataState<Boolean>>

    suspend fun unparticipateEvent(userId: String, eventId: String): Flow<DataState<Boolean>>

}