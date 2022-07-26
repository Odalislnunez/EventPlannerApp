package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Dashboard Fragment"
    }
    val text: LiveData<String> = _text
}