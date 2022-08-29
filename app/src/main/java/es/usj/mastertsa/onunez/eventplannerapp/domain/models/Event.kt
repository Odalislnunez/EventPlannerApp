package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import com.google.type.DateTime
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(
    val eventId: String = UUID.randomUUID().toString(),
    val title: String = INFO_NOT_SET,
    val description: String = INFO_NOT_SET,
    val place: String = INFO_NOT_SET,
    val date: DateTime,
    val type: Boolean = false, // 0 = Private, 1 = Public
    val creators: List<User>,
    val participants: List<User>?
): Parcelable
