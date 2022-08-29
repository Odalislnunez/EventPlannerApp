package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.GetAllPublicEventsUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.SaveEventUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicEventsViewModel @Inject constructor(
    private val getAllPublicEventsUseCase: GetAllPublicEventsUseCase
): ViewModel() {
    private val _publicEventState: MutableLiveData<DataState<List<Event>>> = MutableLiveData()
    val publicEventState: LiveData<DataState<List<Event>>>
        get() = _publicEventState

    fun getPublicEvents(){
        viewModelScope.launch {
            getAllPublicEventsUseCase()
                .onEach { dataState ->
                    _publicEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}