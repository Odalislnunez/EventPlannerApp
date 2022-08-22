package es.usj.mastertsa.onunez.eventplannerapp.di

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        auth: FirebaseAuth,
        @FirebaseModule.UsersCollection usersCollection: CollectionReference
    ): ILoginRepository {
        return LoginRepository(
            Activity(), auth, usersCollection
        )
    }
}