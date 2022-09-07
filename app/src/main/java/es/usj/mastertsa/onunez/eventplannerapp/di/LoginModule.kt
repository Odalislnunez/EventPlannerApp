package es.usj.mastertsa.onunez.eventplannerapp.di

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations.LoginRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.ILoginRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideLoginRepository(
        @ActivityContext activity: Activity,
        auth: FirebaseAuth,
        @FirebaseModule.UsersCollection usersCollection: CollectionReference
    ): ILoginRepository {
        return LoginRepository(
            activity, auth, usersCollection
        )
    }
}