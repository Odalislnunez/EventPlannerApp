package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    suspend fun getAllUsers(): Flow<DataState<List<User>>>

    suspend fun getUserContact(userId: String): Flow<DataState<List<User>>>

    suspend fun getUserNoContact(userId: String): Flow<DataState<List<User>>>

    suspend fun getUserDataInObject(userId: String): Flow<DataState<User>>

    suspend fun saveUser(user: User): Flow<DataState<Boolean>>

    fun saveProfileImage(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment)

}