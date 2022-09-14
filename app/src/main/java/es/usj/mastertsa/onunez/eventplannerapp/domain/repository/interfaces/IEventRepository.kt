package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IEventRepository {

    suspend fun getAllEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getUncomingEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getPastEvents(userId: String): Flow<DataState<List<Event>>>

    suspend fun getAllPublicEvents(): Flow<DataState<List<Event>>>

    suspend fun saveEvent(event: Event, participants: List<String>): Flow<DataState<Boolean>>

    suspend fun getEventCreators(creators: List<String>): Flow<DataState<List<User>>>

    suspend fun getEventParticipants(participants: List<String>): Flow<DataState<List<User>>>

    suspend fun participateEvent(userId: String, eventId: String): Flow<DataState<Boolean>>

    suspend fun unparticipateEvent(userId: String, eventId: String): Flow<DataState<Boolean>>

    suspend fun getInvitations(userId: String): Flow<DataState<List<Event>>>

    suspend fun getInvitation(userId: String, eventId: String): Flow<DataState<Invitation>>

    suspend fun getInvitationsList(eventId: String): Flow<DataState<List<String>>>

    suspend fun getUserContact(userId: String): Flow<DataState<List<User>>>

}