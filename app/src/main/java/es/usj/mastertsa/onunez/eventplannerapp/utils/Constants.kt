package es.usj.mastertsa.onunez.eventplannerapp.utils

object Constants {
    const val INFO_NOT_SET = "info_not_set"
    const val USER_NOT_LOGGED = "user_not_logged"

    const val USERS_COLLECTION = "user"
    const val EVENTS_COLLECTION = "event"
    const val CONTACTS_COLLECTION = "contact"
    const val INVITATIONS_COLLECTION = "invitation"

    var USER_LOGGED_IN_ID = USER_NOT_LOGGED
    var USER_LOGGED_IN_NAME = USER_NOT_LOGGED
    var USER_LOGGED_IN_EMAIL = USER_NOT_LOGGED

    const val USER_NOT_EXISTS : String = "There is no user record corresponding to this identifier. The user may have been deleted."
    const val WRONG_PASSWORD : String = "The password is invalid or the user does not have a password."

    const val VALUE_REQUIRED = "Campo necesario"

    const val EMAIL_ALREADY_EXISTS : String = "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."

    // Shared preferences
    const val ENCRYPTED_SHARED_PREFERENCES_NAME = "EVENTPLANNER_SHARED"

    const val SHARED_EMAIL = "email"
    const val SHARED_PASSWORD = "password"
    const val SHARED_LOGIN_TYPE = "login_type"

    const val DEFAULT_PROFILE_IMAGE = "https://cdn3.iconfinder.com/data/icons/vector-icons-6/96/256-512.png"
    const val EXTRAS_EVENT = "event"
    const val EXTRAS_USER = "user"
}