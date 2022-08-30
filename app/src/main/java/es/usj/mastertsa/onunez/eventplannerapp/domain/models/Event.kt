package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(
    val eventId: String = UUID.randomUUID().toString(),
    val event_title: String = INFO_NOT_SET,
    val event_description: String = INFO_NOT_SET,
    val event_place: String = INFO_NOT_SET,
    val event_date: String = INFO_NOT_SET,
    val event_type: Int = 0, // 0 = Private, 1 = Public
    val event_creators: List<String> = mutableListOf(),
    val event_participants: List<String>? = mutableListOf()
) : Parcelable
