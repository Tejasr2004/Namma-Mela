package com.nammamela.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingId: String,
    val showId: String,
    val showTitle: String,
    val dateMillis: Long,
    val seatIds: String, // Comma separated list of seats
    val price: Double
)
