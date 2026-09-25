package com.danielguillaumont.wheresthemini.data.repository

import com.danielguillaumont.wheresthemini.data.local.ParkingDao
import com.danielguillaumont.wheresthemini.data.local.ParkingEntity
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParkingRepository(
    private val parkingDao: ParkingDao
) {

    val activeParking:
            Flow<ParkingSession?> =
        parkingDao
            .observeActiveParking()
            .map { entity ->

                entity?.toDomain()
            }

    val parkingHistory:
            Flow<List<ParkingSession>> =
        parkingDao
            .observeParkingHistory()
            .map { entities ->

                entities.map { entity ->
                    entity.toDomain()
                }
            }

    suspend fun saveParking(
        parking: ParkingSession
    ) {
        parkingDao.upsertParking(
            parking.toEntity(
                isActive = true
            )
        )
    }

    suspend fun markParkingRecovered(
        parkingId: Long
    ) {
        parkingDao.markParkingRecovered(
            parkingId = parkingId,
            recoveredAtMillis =
                System.currentTimeMillis()
        )
    }
}

private fun ParkingEntity.toDomain():
        ParkingSession {

    val savedLocation =
        if (
            latitude != null &&
            longitude != null &&
            accuracyMeters != null
        ) {

            ParkingLocation(
                latitude = latitude,
                longitude = longitude,
                accuracyMeters = accuracyMeters
            )

        } else {
            null
        }

    return ParkingSession(
        id = id,
        parkingLevel = parkingLevel,
        spotNumber = spotNumber,
        note = note,
        parkingExpiry = parkingExpiry,
        parkedAtMillis = parkedAtMillis,
        location = savedLocation,
        recoveredAtMillis = recoveredAtMillis,
        parkingExpiryMillis = parkingExpiryMillis,
        reminderEnabled = reminderEnabled
    )
}

private fun ParkingSession.toEntity(
    isActive: Boolean
): ParkingEntity {

    return ParkingEntity(
        id = id,
        parkingLevel = parkingLevel,
        spotNumber = spotNumber,
        note = note,
        parkingExpiry = parkingExpiry,
        parkedAtMillis = parkedAtMillis,
        latitude = location?.latitude,
        longitude = location?.longitude,
        accuracyMeters =
            location?.accuracyMeters,
        isActive = isActive,
        recoveredAtMillis =
            recoveredAtMillis,
        parkingExpiryMillis =
            parkingExpiryMillis,
        reminderEnabled =
            reminderEnabled
    )
}