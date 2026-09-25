package com.danielguillaumont.wheresthemini.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class FusedLocationClient(
    context: Context
) {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onSuccess: (ParkingLocation) -> Unit,
        onError: (String) -> Unit
    ) {
        val cancellationTokenSource =
            CancellationTokenSource()

        fusedLocationProviderClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            .addOnSuccessListener { location ->

                if (location == null) {
                    onError(
                        "No location fix was available. Try again in a moment."
                    )

                    return@addOnSuccessListener
                }

                onSuccess(
                    ParkingLocation(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracyMeters = location.accuracy
                    )
                )
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "The Mini's location could not be captured."
                )
            }
    }
}