package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Invitation(
    val invitationId: Int = 0,
    val eventId: String = INFO_NOT_SET,
    val userId: String = INFO_NOT_SET,
    val answer: String = INFO_NOT_SET
): Parcelable
