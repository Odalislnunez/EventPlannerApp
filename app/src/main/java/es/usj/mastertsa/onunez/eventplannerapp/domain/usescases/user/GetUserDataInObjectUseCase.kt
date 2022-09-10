package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.user

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IUserRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataInObjectUseCase @Inject constructor(
    private val userRepository: IUserRepository
){
    suspend operator fun invoke(userId: String): Flow<DataState<User>> =
        userRepository.getUserDataInObject(userId)
}