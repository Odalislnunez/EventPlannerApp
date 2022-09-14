package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInvitationsListUseCase @Inject constructor(
    private val eventRepository: IEventRepository
) {
    suspend operator fun invoke(eventId: String): Flow<DataState<List<String>>> =
        eventRepository.getInvitationsList(eventId)
}