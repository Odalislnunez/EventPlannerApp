package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event

import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event

class EventAddUseCase (private val repository: IEventRepository) {
    suspend fun addEvent(event: Event) {
        repository.addEvent(event)
    }
}