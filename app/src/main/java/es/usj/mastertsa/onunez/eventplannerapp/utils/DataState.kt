package es.usj.mastertsa.onunez.eventplannerapp.utils

import java.lang.Exception

sealed class DataState <out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object Finished : DataState<Nothing>()
}
