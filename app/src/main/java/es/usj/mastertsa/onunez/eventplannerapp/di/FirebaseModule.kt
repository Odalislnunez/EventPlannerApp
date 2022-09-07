package es.usj.mastertsa.onunez.eventplannerapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EVENTS_COLLECTION
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.INVITATIONS_COLLECTION
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USERS_COLLECTION
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @UsersCollection
    @Provides
    @Singleton
    fun provideUsersCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection(USERS_COLLECTION)
    }

    @EventsCollection
    @Provides
    @Singleton
    fun provideEventsCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection(EVENTS_COLLECTION)
    }

    @InvitationsCollection
    @Provides
    @Singleton
    fun provideInvitationsCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection(INVITATIONS_COLLECTION)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class EventsCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InvitationsCollection
}