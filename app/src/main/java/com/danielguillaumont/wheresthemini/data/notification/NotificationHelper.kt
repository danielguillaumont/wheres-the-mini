package com.danielguillaumont.wheresthemini.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.danielguillaumont.wheresthemini.MainActivity

object NotificationHelper {

    private const val CHANNEL_ID =
        "parking_reminders"

    private const val CHANNEL_NAME =
        "Parking reminders"

    private const val CHANNEL_DESCRIPTION =
        "Reminders before your parking expires"

    fun showParkingExpiryReminder(
        context: Context,
        parkingId: Long,
        parkingLevel: String,
        spotNumber: String
    ) {
        createNotificationChannel(
            context
        )

        val appIntent =
            Intent(
                context,
                MainActivity::class.java
            ).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                parkingId
                    .hashCode(),
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val parkingDescription =
            buildParkingDescription(
                parkingLevel = parkingLevel,
                spotNumber = spotNumber
            )

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(
                    "The clock is ticking."
                )
                .setContentText(
                    "$parkingDescription. Your parking expires soon."
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "$parkingDescription. Your parking expires in 15 minutes. Perhaps rescue the Mini before someone starts writing a ticket."
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(
                    true
                )
                .setContentIntent(
                    pendingIntent
                )
                .build()

        NotificationManagerCompat
            .from(
                context
            )
            .notify(
                parkingId.hashCode(),
                notification
            )
    }

    private fun createNotificationChannel(
        context: Context
    ) {
        if (
            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.O
        ) {
            return
        }

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    CHANNEL_DESCRIPTION
            }

        val notificationManager =
            context.getSystemService(
                NotificationManager::class.java
            )

        notificationManager
            .createNotificationChannel(
                channel
            )
    }

    private fun buildParkingDescription(
        parkingLevel: String,
        spotNumber: String
    ): String {
        val level =
            parkingLevel.ifBlank {
                "Unknown level"
            }

        val spot =
            spotNumber.ifBlank {
                "unknown spot"
            }

        return "$level · Spot $spot"
    }
}