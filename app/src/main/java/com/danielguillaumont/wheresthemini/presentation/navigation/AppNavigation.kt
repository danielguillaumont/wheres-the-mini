package com.danielguillaumont.wheresthemini.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()

    val uiState by parkingViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            HomeScreen(
                currentParking = uiState.currentParking,
                onParkedHereClick = {
                    navController.navigate(Routes.SAVE_PARKING)
                },
                onFoundItClick = {
                    parkingViewModel.clearCurrentParking()
                }
            )
        }

        composable(Routes.SAVE_PARKING) {
            SaveParkingScreen(
                formState = uiState.form,
                onParkingLevelChange = parkingViewModel::updateParkingLevel,
                onSpotNumberChange = parkingViewModel::updateSpotNumber,
                onNoteChange = parkingViewModel::updateNote,
                onParkingExpiryChange = parkingViewModel::updateParkingExpiry,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    parkingViewModel.saveParking()
                    navController.popBackStack()
                }
            )
        }
    }
}