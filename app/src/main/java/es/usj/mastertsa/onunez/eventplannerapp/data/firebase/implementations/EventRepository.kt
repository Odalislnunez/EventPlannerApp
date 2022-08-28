package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class EventRepository @Inject constructor (
    @FirebaseModule.EventsCollection private val eventsCollection: CollectionReference
): IEventRepository {

    override suspend fun getAllUserEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection
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

    override suspend fun getUncomingUserEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection
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

    override suspend fun getPastUserEvents(userId: String): Flow<DataState<List<Event>>> = flow {
        emit(DataState.Loading)
        try {
            val publicEvents = eventsCollection
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
}