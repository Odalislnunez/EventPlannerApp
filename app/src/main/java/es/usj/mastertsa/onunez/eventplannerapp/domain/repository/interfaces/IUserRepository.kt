package es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment

interface IUserRepository {

    fun saveProfileImage(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment)

}