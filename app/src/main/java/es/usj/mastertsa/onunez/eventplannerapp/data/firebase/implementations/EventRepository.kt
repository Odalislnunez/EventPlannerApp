package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
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
    private val activity: Activity,
    @FirebaseModule.EventsCollection private val eventsCollection: CollectionReference
): IEventRepository {

    override suspend fun getEvents(userId: String): Flow<DataState<List<Event>>> = flow {
//        var request = false
//        emit(DataState.Loading)
//        try {
//            eventsCollection.document(it)
//                .get()
//                .addOnSuccessListener { document ->
//                    val user = document.toObject(User::class.java)!!
//                    request = true
//                    Constants.USER_LOGGED_IN_ID = user.userId
//                }
//                .addOnFailureListener { request = false }
//                .await()
//            emit(DataState.Success(request))
//            emit(DataState.Finished)
//
//        }catch (e: Exception){
//            emit(DataState.Error(e))
//            emit(DataState.Finished)
//        }

        emit(DataState.Success(mutableListOf()))
    }

    override suspend fun getPublicEvents(): Flow<DataState<List<Event>>> = flow {

//        return commentDao.getComments(placeCode).map { commentList ->
//            commentList.map { comment -> PlaceMapper.mapCommentFromDbToDomain(comment) }
//        }
        emit(DataState.Success(mutableListOf()))
    }

    override suspend fun addEvent(event: Event) {

//        val eventToAdd = Mapper.mapDbToDomainFromEvent(event)
//        commentDao.insertComment(eventToAdd)
    }

    override suspend fun updateEvent(event: Event) {

//        val eventToAdd = Mapper.mapDbToDomainFromEvent(event)
//        commentDao.insertComment(eventToAdd)
    }
}