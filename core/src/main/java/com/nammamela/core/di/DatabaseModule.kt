package com.nammamela.core.di

import android.content.Context
import androidx.room.Room
import com.nammamela.core.data.local.BookingDao
import com.nammamela.core.data.local.NammaMelaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NammaMelaDatabase {
        return Room.databaseBuilder(
            context,
            NammaMelaDatabase::class.java,
            "nammamela.db"
        ).build()
    }

    @Provides
    fun provideBookingDao(database: NammaMelaDatabase): BookingDao {
        return database.bookingDao()
    }
}
