package es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import es.usj.mastertsa.onunez.eventplannerapp.di.FirebaseModule
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IUserRepository
import es.usj.mastertsa.onunez.eventplannerapp.presentation.view.fragments.ProfileFragment
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.StorageUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    @FirebaseModule.UsersCollection private val usersCollection: CollectionReference
): IUserRepository {

    override suspend fun getUserDataInObject(userId: String): Flow<DataState<User>> = flow {
        emit(DataState.Loading)
        try {
            var user = User()
            usersCollection.document(userId)
                .get()
                .addOnSuccessListener { document ->
                    user = document.toObject(User::class.java)!!
                }
                .await()
            emit(DataState.Success(user))
            emit(DataState.Finished)
        }catch (e: Exception){
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }

    override fun saveProfileImage(
        activity: Activity,
        imageFileURI: Uri?,
        imageType: String,
        fragment: Fragment
    ) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType+System.currentTimeMillis()+"."
                    + StorageUtils.getFileExtension(
                activity,
                imageFileURI
            )
        )
        // Adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        when(fragment) {
                            is ProfileFragment -> fragment.uploadImageSuccess(uri.toString())
                        }
                    }
            }
            .addOnFailureListener { exception ->
                // Hide the progress dialog if there is any error. And print the error in log.
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    override suspend fun getAllUsers(): Flow<DataState<List<User>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserContact(userId: String): Flow<DataState<List<User>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserContact(userId: String): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserContact(userId: String): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }


}