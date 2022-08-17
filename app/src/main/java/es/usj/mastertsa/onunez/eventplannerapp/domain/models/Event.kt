package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import com.google.type.DateTime
import java.util.*

data class Event(
    val eventId: Int,
    val title: String,
    val description: String,
    val place: String,
    val date: DateTime,
    val type: Boolean, // 0 = Private, 1 = Public
    val creators: List<User>,
    val participants: List<User>?
)
