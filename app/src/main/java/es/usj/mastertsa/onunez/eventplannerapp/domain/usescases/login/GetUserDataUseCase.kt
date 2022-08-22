package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.login

import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val loginRepository: ILoginRepository
){
    suspend operator fun invoke(): Flow<DataState<Boolean>> =
        loginRepository.getUserData()
}