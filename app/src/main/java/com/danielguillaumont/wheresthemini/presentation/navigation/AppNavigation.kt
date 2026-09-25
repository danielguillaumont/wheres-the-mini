package com.danielguillaumont.wheresthemini.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielguillaumont.wheresthemini.data.local.WheresTheMiniDatabase
import com.danielguillaumont.wheresthemini.data.location.FusedLocationClient
import com.danielguillaumont.wheresthemini.data.navigation.MapNavigator
import com.danielguillaumont.wheresthemini.data.notification.NotificationHelper
import com.danielguillaumont.wheresthemini.data.notification.ParkingReminderScheduler
import com.danielguillaumont.wheresthemini.data.repository.ParkingRepository
import com.danielguillaumont.wheresthemini.presentation.history.HistoryScreen
import com.danielguillaumont.wheresthemini.presentation.home.HomeScreen
import com.danielguillaumont.wheresthemini.presentation.parking.ParkingViewModel
import com.danielguillaumont.wheresthemini.presentation.parking.ParkingViewModelFactory
import com.danielguillaumont.wheresthemini.presentation.parking.SaveParkingScreen

private object Routes {

    const val HOME =
        "home"

    const val SAVE_PARKING =
        "save_parking"

    const val HISTORY =
        "history"
}

@Composable
fun AppNavigation() {

    val context =
        LocalContext.current

    val navController =
        rememberNavController()

    LaunchedEffect(Unit) {
        NotificationHelper
            .ensureNotificationChannel(
                context.applicationContext
            )
    }

    val database =
        remember(context) {
            WheresTheMiniDatabase
                .getDatabase(
                    context
                )
        }

    val repository =
        remember(database) {
            ParkingRepository(
                parkingDao =
                    database.parkingDao()
            )
        }

    val reminderScheduler =
        remember(context) {
            ParkingReminderScheduler(
                context
            )
        }

    val viewModelFactory =
        remember(
            repository,
            reminderScheduler
        ) {
            ParkingViewModelFactory(
                repository =
                    repository,
                reminderScheduler =
                    reminderScheduler
            )
        }

    val parkingViewModel:
            ParkingViewModel =
        viewModel(
            factory =
                viewModelFactory
        )

    val locationClient =
        remember(context) {
            FusedLocationClient(
                context
            )
        }

    val mapNavigator =
        remember(context) {
            MapNavigator(
                context.applicationContext
            )
        }

    val uiState by
    parkingViewModel
        .uiState
        .collectAsState()

    NavHost(
        navController =
            navController,
        startDestination =
            Routes.HOME
    ) {

        composable(
            route =
                Routes.HOME
        ) {

            HomeScreen(
                currentParking =
                    uiState.currentParking,

                lastParking =
                    uiState.parkingHistory
                        .firstOrNull(),

                onParkedHereClick = {

                    if (
                        uiState.currentParking ==
                        null
                    ) {
                        parkingViewModel
                            .beginNewParking()
                    } else {
                        parkingViewModel
                            .beginEditingCurrentParking()
                    }

                    navController
                        .navigate(
                            Routes.SAVE_PARKING
                        )
                },

                onNavigateToMiniClick = {

                    val location =
                        uiState
                            .currentParking
                            ?.location

                    if (
                        location == null
                    ) {

                        Toast
                            .makeText(
                                context,
                                "No saved Mini location is available.",
                                Toast.LENGTH_SHORT
                            )
                            .show()

                    } else {

                        val opened =
                            mapNavigator
                                .openMiniLocation(
                                    location
                                )

                        if (
                            !opened
                        ) {

                            Toast
                                .makeText(
                                    context,
                                    "No maps app or browser could be opened.",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                },

                onFoundItClick = {

                    parkingViewModel
                        .clearCurrentParking()
                },

                onHistoryClick = {

                    navController
                        .navigate(
                            Routes.HISTORY
                        ) {
                            launchSingleTop =
                                true
                        }
                },

                onInfoClick = {}
            )
        }

        composable(
            route =
                Routes.SAVE_PARKING
        ) {

            SaveParkingScreen(
                formState =
                    uiState.form,

                onParkingLevelChange =
                    parkingViewModel::
                    updateParkingLevel,

                onSpotNumberChange =
                    parkingViewModel::
                    updateSpotNumber,

                onNoteChange =
                    parkingViewModel::
                    updateNote,

                onParkingExpirySelected =
                    parkingViewModel::
                    setParkingExpiry,

                onClearParkingExpiry =
                    parkingViewModel::
                    clearParkingExpiry,

                onReminderEnabledChange =
                    parkingViewModel::
                    setReminderEnabled,

                onNotificationPermissionDenied =
                    parkingViewModel::
                    setNotificationPermissionDenied,

                onPhotoCaptured =
                    parkingViewModel::
                    setPhotoPath,

                onRemovePhoto =
                    parkingViewModel::
                    clearPhoto,

                onCaptureLocation = {

                    parkingViewModel
                        .beginLocationCapture()

                    locationClient
                        .getCurrentLocation(
                            onSuccess = {
                                    location ->

                                parkingViewModel
                                    .setCapturedLocation(
                                        location
                                    )
                            },

                            onError = {
                                    message ->

                                parkingViewModel
                                    .setLocationError(
                                        message
                                    )
                            }
                        )
                },

                onLocationPermissionDenied = {

                    parkingViewModel
                        .setLocationPermissionDenied()
                },

                onBackClick = {

                    navController
                        .popBackStack()
                },

                onSaveClick = {

                    parkingViewModel
                        .saveParking()

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            route =
                Routes.HISTORY
        ) {

            HistoryScreen(
                parkingHistory =
                    uiState.parkingHistory,

                onMiniClick = {

                    navController
                        .navigate(
                            Routes.HOME
                        ) {

                            popUpTo(
                                Routes.HOME
                            ) {
                                inclusive =
                                    false
                            }

                            launchSingleTop =
                                true
                        }
                },

                onInfoClick = {}
            )
        }
    }
}