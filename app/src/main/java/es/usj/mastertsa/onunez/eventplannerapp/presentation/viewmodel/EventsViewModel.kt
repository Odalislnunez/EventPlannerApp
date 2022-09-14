package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Invitation
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.*
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
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
    private val getAllPublicEventsUseCase: GetAllPublicEventsUseCase,
    private val getEventCreatorsUseCase: GetEventCreatorsUseCase,
    private val getEventParticipantsUseCase: GetEventParticipantsUseCase,
    private val participateEventUseCase: ParticipateEventUseCase,
    private val unparticipateEventUseCase: UnparticipateEventUseCase,
    private val getInvitationsUseCase: GetInvitationsUseCase,
    private val getInvitationUseCase: GetInvitationUseCase,
    private val getInvitationsListUseCase: GetInvitationsListUseCase,
    private val getUserContactUseCase: GetUserContactUseCase
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
        get() = _publicEventState

    private val _eventCreatorsState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val eventCreatorsState: LiveData<DataState<List<User>>>
        get() = _eventCreatorsState

    private val _eventParticipantsState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val eventParticipantsState: LiveData<DataState<List<User>>>
        get() = _eventParticipantsState

    private val _participateEventState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val participateEventState: LiveData<DataState<Boolean>>
        get() = _participateEventState

    private val _unparticipateEventState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val unparticipateEventState: LiveData<DataState<Boolean>>
        get() = _unparticipateEventState

    private val _getInvitationsEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val getInvitationsEventState: LiveData<DataState<List<Event>>>
        get() = _getInvitationsEventState

    private val _getInvitationEventState: MutableLiveData<DataState<Invitation>> = MutableLiveData()
    val getInvitationEventState: LiveData<DataState<Invitation>>
        get() = _getInvitationEventState

    private val _getInvitationsListState: MutableLiveData<DataState<List<String>>> = MutableLiveData()
    val getInvitationsListState: LiveData<DataState<List<String>>>
        get() = _getInvitationsListState

    private val _getUserContactState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getUserContactState : LiveData<DataState<List<User>>>
        get() =_getUserContactState

    fun saveEvent(event: Event, participants: List<String>){
        viewModelScope.launch {
            saveEventUseCase(event, participants)
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

    fun getEventCreators(users: List<String>){
        viewModelScope.launch {
            getEventCreatorsUseCase(users)
                .onEach { dataState ->
                    _eventCreatorsState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getEventParticipants(users: List<String>){
        viewModelScope.launch {
            getEventParticipantsUseCase(users)
                .onEach { dataState ->
                    _eventParticipantsState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun participateEvent(userId: String, eventId: String){
        viewModelScope.launch {
            participateEventUseCase(userId, eventId)
                .onEach { dataState ->
                    _participateEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun unparticipateEvent(userId: String, eventId: String){
        viewModelScope.launch {
            unparticipateEventUseCase(userId, eventId)
                .onEach { dataState ->
                    _unparticipateEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getInvitationsEvent(userId: String){
        viewModelScope.launch {
            getInvitationsUseCase(userId)
                .onEach { dataState ->
                    _getInvitationsEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getInvitationEvent(userId: String, eventId: String){
        viewModelScope.launch {
            getInvitationUseCase(userId, eventId)
                .onEach { dataState ->
                    _getInvitationEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getInvitationsList(eventId: String){
        viewModelScope.launch {
            getInvitationsListUseCase(eventId)
                .onEach { dataState ->
                    _getInvitationsListState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getUserContact(userId: String){
        viewModelScope.launch {
            getUserContactUseCase(userId)
                .onEach { dataState ->
                    _getUserContactState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}