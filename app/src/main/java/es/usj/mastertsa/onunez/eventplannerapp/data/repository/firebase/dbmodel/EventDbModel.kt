package es.usj.mastertsa.onunez.eventplannerapp.data.repository.firebase.dbmodel

import java.io.Serializable

//@Entity(tableName = TABLE_NAME_PLACE)
data class EventDbModel(
//    @PrimaryKey
    val code:Int,
    val name: String,
    val location: String,
    val description: String,
    val images: String,
    val latitude: String,
    val longitude: String,
    val rating: Double,
    var favorite: String?
) : Serializable