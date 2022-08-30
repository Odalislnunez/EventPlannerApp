package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import android.os.Parcelable
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId: String = INFO_NOT_SET,
    val email: String = INFO_NOT_SET,
    val name: String = INFO_NOT_SET,
    val lastName: String = INFO_NOT_SET,
    val phoneNumber: String = INFO_NOT_SET,
    val profileImage: String = INFO_NOT_SET,
    val userType: Boolean = false // 0 = PERSONA, 1 = EMPRESA
): Parcelable