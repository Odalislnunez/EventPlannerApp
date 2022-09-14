package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import java.util.*

data class Message (
    var message: String = "",
    var from: String = "",
    var userName: String = "",
    var dob: Date = Date()
)