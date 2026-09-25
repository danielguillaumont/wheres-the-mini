package com.danielguillaumont.wheresthemini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_sessions"
)
data class ParkingEntity(

    @PrimaryKey
    val id: Long,

    val parkingLevel: String,

    val spotNumber: String,

    val note: String,

    val parkingExpiry: String,

    val parkedAtMillis: Long,

    val latitude: Double?,

    val longitude: Double?,

    val accuracyMeters: Float?,

    val isActive: Boolean,

    val recoveredAtMillis: Long?,

    val parkingExpiryMillis: Long?,

    val reminderEnabled: Boolean
)