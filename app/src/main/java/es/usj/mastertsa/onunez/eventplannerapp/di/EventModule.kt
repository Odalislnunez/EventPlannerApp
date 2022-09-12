package es.usj.mastertsa.onunez.eventplannerapp.di

import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations.EventRepository
import es.usj.mastertsa.onunez.eventplannerapp.data.firebase.implementations.UserRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IEventRepository
import es.usj.mastertsa.onunez.eventplannerapp.domain.repository.interfaces.IUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {
    @Provides
    @Singleton
    fun provideEventRepository(
        @FirebaseModule.EventsCollection eventsCollection: CollectionReference,
        @FirebaseModule.UsersCollection usersCollection: CollectionReference,
        @FirebaseModule.InvitationsCollection invitationsCollection: CollectionReference
    ): IEventRepository {
        return EventRepository(
            eventsCollection,
            usersCollection,
            invitationsCollection
        )
    }
}