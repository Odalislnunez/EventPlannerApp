package es.usj.mastertsa.onunez.eventplannerapp.domain.models

data class Invitation(
    val invitationId: Int,
    val event: Event,
    val user: User,
    val answer: String
)
