package com.danielguillaumont.wheresthemini.domain.model

data class ParkingLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float
)

data class ParkingSession(
    val id: Long,
    val parkingLevel: String,
    val spotNumber: String,
    val note: String,
    val parkingExpiry: String,
    val parkedAtMillis: Long,
    val location: ParkingLocation? = null
)