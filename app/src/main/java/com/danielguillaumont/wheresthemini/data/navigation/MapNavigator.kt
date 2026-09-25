package com.danielguillaumont.wheresthemini.data.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation

class MapNavigator(
    private val context: Context
) {

    fun openMiniLocation(
        location: ParkingLocation
    ): Boolean {

        val latitude =
            location.latitude

        val longitude =
            location.longitude

        val geoUri =
            Uri.parse(
                "geo:$latitude,$longitude?q=$latitude,$longitude(The Mini)"
            )

        val nativeMapIntent =
            Intent(
                Intent.ACTION_VIEW,
                geoUri
            ).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }

        try {
            context.startActivity(
                nativeMapIntent
            )

            return true

        } catch (
            exception: ActivityNotFoundException
        ) {
            // No native mapping app is available.
        }

        val browserUri =
            Uri.parse(
                "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude"
            )

        val browserIntent =
            Intent(
                Intent.ACTION_VIEW,
                browserUri
            ).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }

        return try {

            context.startActivity(
                browserIntent
            )

            true

        } catch (
            exception: ActivityNotFoundException
        ) {

            false
        }
    }
}