package es.usj.mastertsa.onunez.eventplannerapp.data.repository.implementations

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.usj.mastertsa.onunez.eventplannerapp.data.repository.Mapper
import es.usj.mastertsa.onunez.eventplannerapp.data.repository.firebase.dao.EventDao
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import kotlinx.coroutines.flow.Flow

class EventRepository (
//    private val placeSqLiteHelper: PlaceSqLiteHelper,
    private val eventDao: EventDao
): IEventRepository {
//    private val db = placeSqLiteHelper.writableDatabase
    val db = Firebase.firestore

    override suspend fun getEvents(): Flow<List<Event>>? {

//        return commentDao.getComments(placeCode).map { commentList ->
//            commentList.map { comment -> PlaceMapper.mapCommentFromDbToDomain(comment) }
//        }
        return null;
    }

    override suspend fun addEvents(event: Event) {

//        val eventToAdd = Mapper.mapDbToDomainFromEvent(event)
//        commentDao.insertComment(eventToAdd)
    }
}