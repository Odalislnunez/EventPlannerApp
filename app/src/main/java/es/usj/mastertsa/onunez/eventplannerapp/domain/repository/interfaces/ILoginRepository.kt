package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import android.app.Activity
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {

    suspend fun login(email: String, password: String, loginType: Int, activity: Activity): Flow<DataState<Boolean>>

    suspend fun signUp(user: User, password: String ): Flow<DataState<User>>

    suspend fun logOut(): Flow<DataState<Boolean>>

    suspend fun getUserData(): Flow<DataState<Boolean>>

}