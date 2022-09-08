package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.Event
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.event.SaveEventUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login.GetUserDataUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.profile.GetUserDataInObjectUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.profile.SaveProfileImageUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.signup.SaveUserToFirestoreUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveUserToFirestoreUseCase: SaveUserToFirestoreUseCase,
    private val saveProfileImageUseCase: SaveProfileImageUseCase,
    private val getUserDataInObjectUseCase: GetUserDataInObjectUseCase
): ViewModel() {

    private val _saveUserState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveUserState : LiveData<DataState<Boolean>>
        get() =_saveUserState

    private val _getUserDataInObjectState: MutableLiveData<DataState<User>> = MutableLiveData()
    val getUserDataInObjectState : LiveData<DataState<User>>
        get() =_getUserDataInObjectState

    fun saveUser(user: User){
        viewModelScope.launch {
            saveUserToFirestoreUseCase(user)
                .onEach { dataState ->
                    _saveUserState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getUserInObjectData(userId: String){
        viewModelScope.launch {
             getUserDataInObjectUseCase(userId)
                .onEach { dataState ->
                    _getUserDataInObjectState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun testGetData(userId: String) : User = getUserDataInObjectUseCase.test(userId)

    fun saveProfileImage(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment){
        saveProfileImageUseCase(activity, imageFileURI, imageType, fragment)
    }
}