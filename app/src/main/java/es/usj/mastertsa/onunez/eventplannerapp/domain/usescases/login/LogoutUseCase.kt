package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login

import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val loginRepository: ILoginRepository
) {
    suspend operator fun invoke(): Flow<DataState<Boolean>> =
        loginRepository.logOut()
}