package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.user.SaveUserUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.signup.SignUpUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val saveUserUseCase: SaveUserUseCase
): ViewModel() {
    private val _signUpState: MutableLiveData<DataState<User>> = MutableLiveData()
    val signUpState : LiveData<DataState<User>>
        get() =_signUpState

    private val _saveUserState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val saveUserState : LiveData<DataState<Boolean>>
        get() =_saveUserState

    fun signUp(user : User, password : String){
        viewModelScope.launch {
            signUpUseCase(user, password)
                .onEach { dataState ->
                    _signUpState.value = dataState
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
}