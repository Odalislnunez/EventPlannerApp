package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.user.*
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getUserContactUseCase: GetUserContactUseCase,
    private val getUserNoContactUseCase: GetUserNoContactUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val saveProfileImageUseCase: SaveProfileImageUseCase,
    private val getUserDataInObjectUseCase: GetUserDataInObjectUseCase
): ViewModel() {

    private val _getAllUsersState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getAllUsersState : LiveData<DataState<List<User>>>
        get() =_getAllUsersState

    private val _getUserContactState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getUserContactState : LiveData<DataState<List<User>>>
        get() =_getUserContactState

    private val _getUserNoContactState: MutableLiveData<DataState<List<User>>> = MutableLiveData()
    val getUserNoContactState : LiveData<DataState<List<User>>>
        get() =_getUserNoContactState

    private val _saveUserState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveUserState : LiveData<DataState<Boolean>>
        get() =_saveUserState

    private val _getUserDataInObjectState: MutableLiveData<DataState<User>> = MutableLiveData()
    val getUserDataInObjectState : LiveData<DataState<User>>
        get() =_getUserDataInObjectState

    fun getAllUsers(userId: String){
        viewModelScope.launch {
            getAllUsersUseCase()
                .onEach { dataState ->
                    _getAllUsersState.value = dataState
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

    fun getUserNoContact(userId: String){
        viewModelScope.launch {
            getUserNoContactUseCase(userId)
                .onEach { dataState ->
                    _getUserNoContactState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun saveUser(user: User){
        viewModelScope.launch {
            saveUserUseCase(user)
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

    fun saveProfileImage(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment){
        saveProfileImageUseCase(activity, imageFileURI, imageType, fragment)
    }

}