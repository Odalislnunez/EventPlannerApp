package es.usj.mastertsa.onunez.eventplannerapp.domain.models

data class Contact(
    val userId: Int,
    val contact_users: List<Int>
)
