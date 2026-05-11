package com.nammamela.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
abstract class NammaMelaDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}
