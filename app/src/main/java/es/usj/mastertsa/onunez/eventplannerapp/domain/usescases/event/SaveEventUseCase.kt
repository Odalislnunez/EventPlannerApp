package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val eventRepository: IEventRepository
) {
    suspend operator fun invoke(event: Event): Flow<DataState<Boolean>> =
        eventRepository.saveEvent(event)
}