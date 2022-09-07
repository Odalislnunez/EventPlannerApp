package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INFO_NOT_SET
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_EMAIL
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_ID
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_NAME
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

    private val callbackManager = CallbackManager.Factory.create()

    override suspend fun login(email: String, password: String, loginType: Int, activity: Activity): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var isSuccesful = false
            if (loginType == 0) { // LOGIN WITH EMAIL AND PASSWORD

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { isSuccesful = true }
                    .addOnFailureListener { isSuccesful = false }
                    .await()
            }
            else if (loginType == 1) { // LOGIN WITH FACEBOOK

                LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email"))

                LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult) {
                            result.let { it ->
                                val token = it.accessToken

                                val credential = FacebookAuthProvider.getCredential(token.token)

                                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { isSuccesful = it.isSuccessful }
                            }
                        }
                        override fun onCancel() { TODO("Not yet implemented") }
                        override fun onError(error: FacebookException) { TODO("Not yet implemented") }
                    })
            }
            emit(DataState.Success(isSuccesful))
        } catch (exc: Exception) {
            emit(DataState.Error(exc))
        }
        emit(DataState.Finished)
    }

    override suspend fun signUp(user: User, password: String): Flow<DataState<User>> = flow {
        emit(DataState.Loading)
        try {
            lateinit var exception: Exception
            var registeredUser: User = User()
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    registeredUser = User(
                        userId = it.user!!.uid,
                        email = user.email,
                        name = user.name,
                        lastname = user.lastname,
                        phoneNumber = user.phoneNumber,
                        userType = user.userType
                    )
                }
                .addOnFailureListener { exception = it!! }
                .await()
            if (registeredUser.userId != INFO_NOT_SET){
                emit(DataState.Success(registeredUser))
                emit(DataState.Finished)
            } else{
                emit(DataState.Error(exception))
                emit(DataState.Finished)
            }
        }catch (exc: Exception){
            emit(DataState.Error(exc))
            emit(DataState.Finished)
        }
    }

    override suspend fun logOut(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            auth.signOut()
            emit(DataState.Success(true))
            emit(DataState.Finished)
        } catch (exc: Exception) {
            emit(DataState.Error(exc))
            emit(DataState.Finished)
        }
    }

    override suspend fun getUserData(): Flow<DataState<Boolean>> = flow {
        var request = false
        val currentUser = auth.currentUser
        emit(DataState.Loading)
        try {
            currentUser?.uid?.let {
                usersCollection.document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)!!
                        request = true
                        USER_LOGGED_IN_ID = user.userId
                        USER_LOGGED_IN_NAME = user.name + " " + user.lastname
                        USER_LOGGED_IN_EMAIL = user.email
                    }
                    .addOnFailureListener { request = false }
                    .await()
            }
            emit(DataState.Success(request))
            emit(DataState.Finished)

        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override suspend fun saveUserToFirestore(user: User): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var isSuccessful = false
            usersCollection.document(user.userId).set(user, SetOptions.merge())
                .addOnSuccessListener { isSuccessful = true }
                .addOnFailureListener { isSuccessful = false }
                .await()
            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        } catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

}