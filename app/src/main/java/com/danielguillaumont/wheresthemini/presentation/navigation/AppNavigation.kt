package com.danielguillaumont.wheresthemini.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielguillaumont.wheresthemini.data.location.FusedLocationClient
import com.danielguillaumont.wheresthemini.presentation.home.HomeScreen
import com.danielguillaumont.wheresthemini.presentation.parking.ParkingViewModel
import com.danielguillaumont.wheresthemini.presentation.parking.SaveParkingScreen

private object Routes {
    const val HOME = "home"
    const val SAVE_PARKING = "save_parking"
}

@Composable
fun AppNavigation(
    parkingViewModel: ParkingViewModel = viewModel()
) {
    val navController =
        rememberNavController()

    val context =
        LocalContext.current

    val locationClient =
        remember(context) {
            FusedLocationClient(
                context
            )
        }

    val uiState by
    parkingViewModel
        .uiState
        .collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(
            route = Routes.HOME
        ) {

            HomeScreen(
                currentParking =
                    uiState.currentParking,

                onParkedHereClick = {

                    if (
                        uiState.currentParking == null
                    ) {
                        parkingViewModel
                            .beginNewParking()
                    } else {
                        parkingViewModel
                            .beginEditingCurrentParking()
                    }

                    navController.navigate(
                        Routes.SAVE_PARKING
                    )
                },

                onFoundItClick = {
                    parkingViewModel
                        .clearCurrentParking()
                }
            )
        }

        composable(
            route = Routes.SAVE_PARKING
        ) {

            SaveParkingScreen(
                formState =
                    uiState.form,

                onParkingLevelChange =
                    parkingViewModel::updateParkingLevel,

                onSpotNumberChange =
                    parkingViewModel::updateSpotNumber,

                onNoteChange =
                    parkingViewModel::updateNote,

                onParkingExpiryChange =
                    parkingViewModel::updateParkingExpiry,

                onCaptureLocation = {

                    parkingViewModel
                        .beginLocationCapture()

                    locationClient
                        .getCurrentLocation(

                            onSuccess = { location ->

                                parkingViewModel
                                    .setCapturedLocation(
                                        location
                                    )
                            },

                            onError = { message ->

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
    }
}