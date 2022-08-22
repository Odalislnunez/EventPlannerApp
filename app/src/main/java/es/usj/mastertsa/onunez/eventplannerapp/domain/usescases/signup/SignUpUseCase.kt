package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.signup

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val loginRepository: ILoginRepository
) {
    suspend operator fun invoke(user: User, password: String): Flow<DataState<User>> =
        loginRepository.signUp(user, password)
}