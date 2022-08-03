package es.usj.mastertsa.onunez.eventplannerapp.domain.models

data class User(
    val code: Int, val user: String, val password: String, val name: String, val lastname: String,
    val phone_number: String, val user_type: String
)