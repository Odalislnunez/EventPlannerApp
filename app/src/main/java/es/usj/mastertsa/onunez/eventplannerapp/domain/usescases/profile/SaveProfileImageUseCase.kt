package es.usj.mastertsa.onunez.eventplannerapp.domain.usescases.profile

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IUserRepository
import javax.inject.Inject

class SaveProfileImageUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    operator fun invoke(activity: Activity, imageFileURI: Uri?, imageType: String, fragment: Fragment) =
        userRepository.saveProfileImage(activity, imageFileURI, imageType, fragment)
}