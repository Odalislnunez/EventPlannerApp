package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun saveProfileImage(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment)

}