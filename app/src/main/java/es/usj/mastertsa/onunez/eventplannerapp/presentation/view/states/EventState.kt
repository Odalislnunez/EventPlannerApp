package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.states

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event

sealed class EventState {
    object Loading : EventState()
    data class Success(val data: List<Event>) : EventState()
    data class Failure(val exception: Throwable) : EventState()
}