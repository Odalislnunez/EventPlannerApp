package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import androidx.compose.ui.res.fontResource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObjects
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.sql.Timestamp
import javax.inject.Inject

class EventRepository @Inject constructor (
    @FirebaseModule.EventsCollection private val eventsCollection: CollectionReference,
    @FirebaseModule.UsersCollection private val usersCollection: CollectionReference,
    @FirebaseModule.InvitationsCollection private val invitationsCollection: CollectionReference
): IEventRepository {

    override suspend fun getAllEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val allEvents = eventsCollection.whereIn("creators", userId.toList())
                .get()
                .await()
                .toObjects(Event::class.java)
            emit(DataState.Success(allEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getUncomingEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            var uncomingEvents: List<Event> = mutableListOf()
            val uCreatorsEvents = eventsCollection.whereArrayContainsAny("creators", listOf(userId))
                .get()
                .await()
                .toObjects(Event::class.java)
            val uParticipantsEvents = eventsCollection.whereArrayContainsAny("participants", listOf(userId))
                .get()
                .await()
                .toObjects(Event::class.java)

            (uCreatorsEvents + uParticipantsEvents).forEach {
                if (Timestamp.valueOf(it.datetime) >= Timestamp(System.currentTimeMillis())) {
                    uncomingEvents = uncomingEvents + it
                }
            }
            emit(DataState.Success(uncomingEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getPastEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            var pastEvents: List<Event> = mutableListOf()
            val pCreatorsEvents = eventsCollection.whereArrayContainsAny("creators", listOf(userId))
                .get()
                .await()
                .toObjects(Event::class.java)
            val pParticipantsEvents = eventsCollection.whereArrayContainsAny("participants", listOf(userId))
                .get()
                .await()
                .toObjects(Event::class.java)

            (pCreatorsEvents + pParticipantsEvents).forEach {
                if (Timestamp.valueOf(it.datetime) < Timestamp(System.currentTimeMillis())) {
                    pastEvents = pastEvents + it
                }
            }
            emit(DataState.Success(pastEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getAllPublicEvents(): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection.whereEqualTo("type", 1)
                .get()
                .await()
                .toObjects(Event::class.java)
            emit(DataState.Success(publicEvents))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun saveEvent(event: Event, participants: List<String>) = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful = false

            if (event.type == 0) {
                val invitations = invitationsCollection.whereEqualTo("eventId", event.eventId)
                    .get()
                    .await()
                    .toObjects(Invitation::class.java)

                val invitationUsers: MutableList<String> = mutableListOf()

                invitations.forEach {
                    invitationUsers.add(it.userId)
                    if (event.participants?.contains(it.userId) == true && !participants.contains(it.userId)) {
                        saveInvitation(it.invitationId, it.userId, event.eventId, 3)
                    }
                }

                participants.forEach {
                    if (event.participants?.contains(it) == false && !invitationUsers.contains(it)){
                        saveInvitation("", it, event.eventId, 0)
                    }
                }
            }

            event.participants = participants

            eventsCollection.document(event.eventId).set(event, SetOptions.merge())
                .addOnSuccessListener { isSuccessful = true }
                .addOnFailureListener { isSuccessful = false }
                .await()
            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        } catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getEventCreators(creators: List<String>): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        try {
            val creatorsList: MutableList<User> = ArrayList()
            creators.forEach {
                val user = usersCollection.whereEqualTo("userId", it)
                    .get()
                    .await()
                    .toObjects(User::class.java)
                creatorsList.addAll(user)
            }
            emit(DataState.Success(creatorsList))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getEventParticipants(participants: List<String>): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        try {
            val participantsList: MutableList<User> = ArrayList()
            participants.forEach {
                val user = usersCollection.whereEqualTo("userId", it)
                    .get()
                    .await()
                    .toObjects(User::class.java)
                participantsList.addAll(user)
            }
            emit(DataState.Success(participantsList))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun participateEvent(userId: String, eventId: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful = false
            val event = eventsCollection.whereEqualTo("eventId", eventId)
                .get()
                .await()
                .toObjects(Event::class.java)[0]

            event.participants = event.participants?.plus(userId)

            eventsCollection.document(event.eventId).set(event, SetOptions.merge())
                .addOnSuccessListener { isSuccessful = true }
                .addOnFailureListener { isSuccessful = false }
                .await()

            val invitation = invitationsCollection.whereEqualTo("eventId", event.eventId).whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Invitation::class.java)[0]

            saveInvitation(invitation.invitationId, userId, event.eventId, 1)

            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun unparticipateEvent(userId: String, eventId: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful = false
            val event = eventsCollection.whereEqualTo("eventId", eventId)
                .get()
                .await()
                .toObjects(Event::class.java)[0]

            event.participants = event.participants?.minus(userId)

            eventsCollection.document(event.eventId).set(event, SetOptions.merge())
                .addOnSuccessListener { isSuccessful = true }
                .addOnFailureListener { isSuccessful = false }
                .await()

            val invitation = invitationsCollection.whereEqualTo("eventId", event.eventId).whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Invitation::class.java)[0]

            saveInvitation(invitation.invitationId, userId, event.eventId, 2)

            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getInvitations(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            var invitations: List<Event> = mutableListOf()
            val iPending = invitationsCollection.whereEqualTo("userId", userId)
                .whereEqualTo("answer", 0)
                .get()
                .await()
                .toObjects(Invitation::class.java)
            val iRejected = invitationsCollection.whereEqualTo("userId", userId)
                .whereEqualTo("answer", 2)
                .get()
                .await()
                .toObjects(Invitation::class.java)

            val eventIds: MutableList<String> = mutableListOf()
            (iPending + iRejected).forEach {
                eventIds.add(it.eventId)
            }

            val events = eventsCollection.whereIn("eventId", eventIds)
                .get()
                .await()
                .toObjects(Event::class.java)

            events.forEach {
                if (Timestamp.valueOf(it.datetime) >= Timestamp(System.currentTimeMillis())) {
                    invitations = invitations + it
                }
            }

            emit(DataState.Success(invitations))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getUserContact(userId: String): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        try {
            var contacts: List<User> = mutableListOf()
            val user = usersCollection.whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(User::class.java)[0]

            user.contacts.forEach {
                val userContact = usersCollection.whereEqualTo("userId", it)
                    .get()
                    .await()
                    .toObjects(User::class.java)[0]

                contacts = contacts + userContact
            }
            emit(DataState.Success(contacts))
            emit(DataState.Finished)
        }catch (e: Exception) {
//            emit(DataState.Error(e))
//            emit(DataState.Finished)
        }
    }

    suspend fun saveInvitation(invitationId: String, userId: String, eventId: String, answer: Int): Boolean {
        var isSuccessful = false
        val invitation: Invitation
        if (invitationId.isNotEmpty()) {
            invitation = Invitation (
                invitationId = invitationId,
                userId = userId,
                eventId = eventId,
                answer = answer
            )
        }
        else {
            invitation = Invitation (
                userId = userId,
                eventId = eventId,
                answer = answer
            )
        }

        invitationsCollection.document(invitation.invitationId).set(invitation, SetOptions.merge())
            .addOnSuccessListener { isSuccessful = true }
            .addOnFailureListener { isSuccessful = false }
            .await()

        return isSuccessful
    }

}