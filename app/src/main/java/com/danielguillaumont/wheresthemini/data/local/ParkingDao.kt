package com.danielguillaumont.wheresthemini.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingDao {

    @Query(
        """
        SELECT *
        FROM parking_sessions
        WHERE isActive = 1
        ORDER BY parkedAtMillis DESC
        LIMIT 1
        """
    )
    fun observeActiveParking():
            Flow<ParkingEntity?>

    @Query(
        """
        SELECT *
        FROM parking_sessions
        WHERE isActive = 0
        ORDER BY recoveredAtMillis DESC
        """
    )
    fun observeParkingHistory():
            Flow<List<ParkingEntity>>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun upsertParking(
        parking: ParkingEntity
    )

    @Query(
        """
        UPDATE parking_sessions
        SET isActive = 0,
            recoveredAtMillis = :recoveredAtMillis
        WHERE id = :parkingId
        """
    )
    suspend fun markParkingRecovered(
        parkingId: Long,
        recoveredAtMillis: Long
    )
}