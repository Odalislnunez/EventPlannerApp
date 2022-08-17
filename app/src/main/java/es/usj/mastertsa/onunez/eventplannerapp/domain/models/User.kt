package es.usj.mastertsa.onunez.eventplannerapp.domain.models

data class User(
    val userId: Int,
    val email: String,
    val password: String,
    val name: String,
    val lastname: String,
    val phoneNumber: String,
    val userType: Boolean // 0 = PERSONA, 1 = EMPRESA
)