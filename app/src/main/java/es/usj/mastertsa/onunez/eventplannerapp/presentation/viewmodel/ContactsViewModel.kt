package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.user.GetUserContactUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.user.GetUserNoContactUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getUserContactUseCase: GetUserContactUseCase,
    private val getUserNoContactUseCase: GetUserNoContactUseCase
): ViewModel() {

    private val _getUserContactState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getUserContactState : LiveData<DataState<List<User>>>
        get() =_getUserContactState

    private val _getUserNoContactState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getUserNoContactState : LiveData<DataState<List<User>>>
        get() =_getUserNoContactState

    fun getUserContact(userId: String){
        viewModelScope.launch {
            getUserContactUseCase(userId)
                .onEach { dataState ->
                    _getUserContactState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getUserNoContact(userId: String){
        viewModelScope.launch {
            getUserNoContactUseCase(userId)
                .onEach { dataState ->
                    _getUserNoContactState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}