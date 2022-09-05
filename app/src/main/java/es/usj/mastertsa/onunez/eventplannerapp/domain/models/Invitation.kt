package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Invitation(
    val eventId: String = INFO_NOT_SET,
    val userId: String = INFO_NOT_SET,
    val answer: Int = 0 // 0 = Pendiente, 1 = Aceptada, 2 = Rechazada, 3 = Cancelada
): Parcelable
