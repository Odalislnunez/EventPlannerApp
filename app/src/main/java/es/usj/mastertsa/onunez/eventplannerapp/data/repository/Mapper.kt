package es.usj.mastertsa.onunez.eventplannerapp.data.repository

import es.usj.mastertsa.onunez.eventplannerapp.data.repository.firebase.dbmodel.EventDbModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event

object Mapper {
    fun mapEventFromDbToDomain(eventDbModel: EventDbModel): Event {
        return Event(
            0,
            "",
            "",
            "",
            "",
            false,
            null,
            null
        )
    }

    fun mapDbToDomainFromEvent(event: Event): EventDbModel {
        return EventDbModel(
            event.code,
            "",
            "",
            event.description,
            "",
            "",
            "",
            0.00,
            ""
        )
    }
}