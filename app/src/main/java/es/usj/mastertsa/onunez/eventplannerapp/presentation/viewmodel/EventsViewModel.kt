package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.*
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val saveEventUseCase: SaveEventUseCase,
    private val getUncomingUserEventsUseCase: GetUncomingUserEventsUseCase,
    private val getPastUserEventsUseCase: GetPastUserEventsUseCase,
    private val getAllUserEventsUseCase: GetAllUserEventsUseCase,
    private val getAllPublicEventsUseCase: GetAllPublicEventsUseCase
): ViewModel() {

    private val _saveEventState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveEventState: LiveData<DataState<Boolean>>
        get() = _saveEventState

    private val _uncomingEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val uncomingEventState: LiveData<DataState<List<Event>>>
        get() = _uncomingEventState

    private val _pastEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val pastEventState: LiveData<DataState<List<Event>>>
        get() = _pastEventState

    private val _allUserEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val allUserEventState: LiveData<DataState<List<Event>>>
        get() = _allUserEventState

    private val _publicEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val publicEventState: LiveData<DataState<List<Event>>>
        get() = _allUserEventState

    fun saveEvent(event: Event){
        viewModelScope.launch {
            saveEventUseCase(event)
                .onEach { dataState ->
                    _saveEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

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

    fun getPublicEvents(){
        viewModelScope.launch {
            getAllPublicEventsUseCase()
                .onEach { dataState ->
                    _publicEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}