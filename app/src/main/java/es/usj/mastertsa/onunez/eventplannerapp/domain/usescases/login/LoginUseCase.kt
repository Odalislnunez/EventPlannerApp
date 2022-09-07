package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login

import android.app.Activity
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: ILoginRepository
){
    suspend operator fun invoke(email: String, password: String, loginType: Int, activity: Activity): Flow<DataState<Boolean>> =
        loginRepository.login(email, password, loginType, activity)
}