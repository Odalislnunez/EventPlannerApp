package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {

    suspend fun login(email: String, password: String, loginType: Int): Flow<DataState<Boolean>>

    suspend fun signUp(user: User, password: String ): Flow<DataState<User>>

    suspend fun logOut(): Flow<DataState<Boolean>>

    suspend fun getUserData(): Flow<DataState<Boolean>>

    suspend fun saveUser(user: User): Flow<DataState<Boolean>>
}