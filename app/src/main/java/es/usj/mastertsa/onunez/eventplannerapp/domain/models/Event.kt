package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import com.google.type.DateTime
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import java.util.*

data class Event(
    val eventId: Int,
    val title: String = INFO_NOT_SET,
    val description: String = INFO_NOT_SET,
    val place: String = INFO_NOT_SET,
    val date: DateTime,
    val type: Boolean = false, // 0 = Private, 1 = Public
    val creators: List<User>,
    val participants: List<User>?
)
