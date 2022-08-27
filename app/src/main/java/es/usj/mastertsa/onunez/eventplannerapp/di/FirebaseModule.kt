package es.usj.mastertsa.onunez.eventplannerapp.di

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EVENTS_COLLECTION
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USERS_COLLECTION
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(
        @ApplicationContext context: Context
    ): FirebaseAuth {
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class EventsCollection
}