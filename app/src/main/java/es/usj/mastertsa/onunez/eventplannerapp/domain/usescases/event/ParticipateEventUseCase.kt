package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParticipateEventUseCase @Inject constructor(
    private val eventRepository: IEventRepository
){
    suspend operator fun invoke(userId: String, eventId: String): Flow<DataState<Boolean>> =
        eventRepository.participateEvent(userId, eventId)
}