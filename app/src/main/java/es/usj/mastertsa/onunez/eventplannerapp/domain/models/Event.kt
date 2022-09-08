package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.util.*

@Parcelize
data class Event(
    val eventId: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = INFO_NOT_SET,
    val place: String = INFO_NOT_SET,
    val datetime: String = INFO_NOT_SET,//Timestamp.valueOf("2022-09-14 00:00:00.123456789"),
    val type: Int = 0, // 0 = Private, 1 = Public
    val creators: List<String> = mutableListOf(),
    val participants: List<String>? = mutableListOf(),
    val status: Int = 0 // 0 = Pendiente, 1 = Pasado, 2 = Pospuesto, 3 = Cancelado
) : Parcelable
