package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.SaveEventUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val saveEventUseCase: SaveEventUseCase
): ViewModel() {
    private val _saveEventState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveEventState: LiveData<DataState<Boolean>>
        get() = _saveEventState

    fun saveEvent(event: Event){
        viewModelScope.launch {
            saveEventUseCase(event)
                .onEach { dataState ->
                    _saveEventState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}