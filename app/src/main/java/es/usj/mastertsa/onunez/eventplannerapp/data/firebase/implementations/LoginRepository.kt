package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @FirebaseModule.UsersCollection private val usersCollection: CollectionReference
        ): ILoginRepository {

    override suspend fun login(email: String, password: String, loginType: Int): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var isSuccesful: Boolean = false
            if (loginType == 0) // LOGIN WITH EMAIL AND PASSWORD
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { isSuccesful = true }
                    .addOnFailureListener { isSuccesful = false }
                .await()
        } catch (exc: Exception) {

        }
    }

    override suspend fun signUp(user: User, password: String): Flow<DataState<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun logOut(): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserData(): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: User): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }
}