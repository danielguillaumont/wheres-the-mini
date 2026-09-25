package com.danielguillaumont.wheresthemini.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ParkingReminderWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParameters
) {

    override suspend fun doWork():
            Result {

        val parkingId =
            inputData.getLong(
                KEY_PARKING_ID,
                -1L
            )

        if (
            parkingId == -1L
        ) {
            return Result.failure()
        }

        val parkingLevel =
            inputData.getString(
                KEY_PARKING_LEVEL
            ).orEmpty()

        val spotNumber =
            inputData.getString(
                KEY_SPOT_NUMBER
            ).orEmpty()

        NotificationHelper
            .showParkingExpiryReminder(
                context =
                    applicationContext,
                parkingId =
                    parkingId,
                parkingLevel =
                    parkingLevel,
                spotNumber =
                    spotNumber
            )

        return Result.success()
    }

    companion object {

        const val KEY_PARKING_ID =
            "parking_id"

        const val KEY_PARKING_LEVEL =
            "parking_level"

        const val KEY_SPOT_NUMBER =
            "spot_number"
    }
}