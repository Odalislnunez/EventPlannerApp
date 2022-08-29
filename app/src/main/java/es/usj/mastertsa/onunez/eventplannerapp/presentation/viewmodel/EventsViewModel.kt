package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.GetAllUserEventsUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.GetPastUserEventsUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.GetUncomingUserEventsUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.SaveEventUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val getUncomingUserEventsUseCase: GetUncomingUserEventsUseCase,
    private val getPastUserEventsUseCase: GetPastUserEventsUseCase,
    private val getAllUserEventsUseCase: GetAllUserEventsUseCase
): ViewModel() {
    private val _uncomingEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val uncomingEventState: LiveData<DataState<List<Event>>>
        get() = _uncomingEventState

    private val _pastEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val pastEventState: LiveData<DataState<List<Event>>>
        get() = _pastEventState

    private val _allUserEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val allUserEventState: LiveData<DataState<List<Event>>>
        get() = _allUserEventState

    fun getUncomingEvents(userId: String){
        viewModelScope.launch {
            getUncomingUserEventsUseCase(userId)
                .onEach { dataState ->
                    _uncomingEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getPastEvents(userId: String){
        viewModelScope.launch {
            getPastUserEventsUseCase(userId)
                .onEach { dataState ->
                    _pastEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getAllUserEvents(userId: String){
        viewModelScope.launch {
            getAllUserEventsUseCase(userId)
                .onEach { dataState ->
                    _allUserEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}