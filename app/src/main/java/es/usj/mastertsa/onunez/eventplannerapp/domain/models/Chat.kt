package es.usj.mastertsa.onunez.eventplannerapp.domain.models

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)