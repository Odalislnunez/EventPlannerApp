package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET

data class User(
    val userId: String = INFO_NOT_SET,
    val email: String = INFO_NOT_SET,
    val givenName: String = INFO_NOT_SET,
    val phoneNumber: String = INFO_NOT_SET,
    val userType: Boolean = false // 0 = PERSONA, 1 = EMPRESA
)