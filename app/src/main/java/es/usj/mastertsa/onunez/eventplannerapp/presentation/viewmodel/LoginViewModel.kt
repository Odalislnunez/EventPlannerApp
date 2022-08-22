package es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login.GetUserDataUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login.LoginUseCase
import es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login.LogoutUseCase
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    private val _loginState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val loginState: LiveData<DataState<Boolean>>
        get() = _loginState

    private val _userDataState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val userDataState: LiveData<DataState<Boolean>>
        get() = _userDataState

    private val _logOutState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val logOutState: LiveData<DataState<Boolean>>
        get() = _logOutState

    fun login(email: String, password: String, loginType: Int){
        viewModelScope.launch {
            loginUseCase(email, password, loginType)
                .onEach { dataState ->
                    _loginState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun getUserData(){
        viewModelScope.launch {
            getUserDataUseCase()
                .onEach { dataState ->
                    _userDataState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    fun logOut(){
        viewModelScope.launch {
            logoutUseCase()
                .onEach { dataState ->
                    _logOutState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}