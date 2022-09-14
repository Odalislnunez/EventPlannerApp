package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvitationUseCase @Inject constructor(
    private val eventRepository: IEventRepository
) {
    suspend operator fun invoke(userId: String, eventId: String): Flow<DataState<Invitation>> =
        eventRepository.getInvitation(userId, eventId)
}