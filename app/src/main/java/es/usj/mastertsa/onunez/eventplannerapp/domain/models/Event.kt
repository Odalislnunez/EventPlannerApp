package es.usj.mastertsa.onunez.eventplannerapp.domain.models

import java.util.*

data class Event(
    val code: Int,
    val title: String,
    val description: String,
    val place: String,
    val date: String,
    val type: Boolean, // 0 = Private, 1 = Public
    val creators: List<Int>?,
    val participants: List<Int>?
)
