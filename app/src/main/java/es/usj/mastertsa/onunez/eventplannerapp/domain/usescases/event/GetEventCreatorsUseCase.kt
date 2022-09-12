package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventCreatorsUseCase @Inject constructor(
    private val eventRepository: IEventRepository
) {
    suspend operator fun invoke(users: List<String>): Flow<DataState<List<User>>> =
        eventRepository.getEventCreators(users)
}