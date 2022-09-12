package es.usj.mastertsa.onunez.eventplannerapp.di

import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations.UserRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InvitationModule {
    @Provides
    @Singleton
    fun provideInvitationRepository(
        @FirebaseModule.UsersCollection usersCollection: CollectionReference
    ): IUserRepository {
        return UserRepository(
            usersCollection
        )
    }
}