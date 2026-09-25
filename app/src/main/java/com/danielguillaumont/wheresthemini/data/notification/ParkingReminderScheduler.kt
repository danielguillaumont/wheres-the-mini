package com.danielguillaumont.wheresthemini.data.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class ParkingReminderScheduler(
    context: Context
) {

    private val workManager =
        WorkManager.getInstance(
            context.applicationContext
        )

    fun scheduleReminder(
        parkingId: Long,
        reminderAtMillis: Long,
        parkingLevel: String,
        spotNumber: String
    ): Boolean {

        val delayMillis =
            reminderAtMillis -
                    System.currentTimeMillis()

        if (
            delayMillis <= 0L
        ) {
            cancelReminder(
                parkingId
            )

            return false
        }

        val inputData =
            workDataOf(
                ParkingReminderWorker
                    .KEY_PARKING_ID to
                        parkingId,

                ParkingReminderWorker
                    .KEY_PARKING_LEVEL to
                        parkingLevel,

                ParkingReminderWorker
                    .KEY_SPOT_NUMBER to
                        spotNumber
            )

        val reminderRequest =
            OneTimeWorkRequestBuilder<
                    ParkingReminderWorker
                    >()
                .setInitialDelay(
                    delayMillis,
                    TimeUnit.MILLISECONDS
                )
                .setInputData(
                    inputData
                )
                .addTag(
                    workName(
                        parkingId
                    )
                )
                .build()

        workManager
            .enqueueUniqueWork(
                workName(
                    parkingId
                ),
                ExistingWorkPolicy.REPLACE,
                reminderRequest
            )

        return true
    }

    fun cancelReminder(
        parkingId: Long
    ) {
        workManager
            .cancelUniqueWork(
                workName(
                    parkingId
                )
            )
    }

    private fun workName(
        parkingId: Long
    ): String {
        return "parking-reminder-$parkingId"
    }
}