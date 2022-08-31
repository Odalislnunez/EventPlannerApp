package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.SaveEventUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}