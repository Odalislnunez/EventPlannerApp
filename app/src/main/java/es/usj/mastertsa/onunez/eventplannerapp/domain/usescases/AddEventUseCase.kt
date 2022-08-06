package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases

import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event

class AddEventUseCase (val repository: IEventRepository) {
    suspend fun addEvent(event: Event) {
        repository.addEvents(event)
    }
}