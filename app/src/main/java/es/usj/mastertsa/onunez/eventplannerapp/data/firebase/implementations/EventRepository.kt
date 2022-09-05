package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
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
    @FirebaseModule.EventsCollection private val eventsCollection: CollectionReference
): IEventRepository {

    override suspend fun getAllEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection.whereIn("creators", userId.toList())
                .get()
                .await()
                .toObjects(Event::class.java)
            emit(DataState.Success(publicEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getUncomingEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection.whereIn("creators", userId.toList())
                .whereGreaterThanOrEqualTo("datetime", Timestamp(System.currentTimeMillis()))
                .get()
                .await()
                .toObjects(Event::class.java)
            emit(DataState.Success(publicEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getPastEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection.whereIn("creators", userId.toList())
                .whereLessThan("datetime", Timestamp(System.currentTimeMillis()))
                .get()
                .await()
                .toObjects(Event::class.java)
            emit(DataState.Success(publicEvents))
            emit(DataState.Finished)
        }catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun getAllPublicEvents(): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection
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

    override suspend fun saveEvent(event: Event) = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful = false
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

    override suspend fun getEventCreators(creators: List<String>): Flow<DataState<List<User>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventParticipants(participants: List<String>): Flow<DataState<List<User>>> {
        TODO("Not yet implemented")
    }

}