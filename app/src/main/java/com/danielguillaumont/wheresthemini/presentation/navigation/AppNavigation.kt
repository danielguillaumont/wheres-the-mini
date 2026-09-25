package com.danielguillaumont.wheresthemini.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielguillaumont.wheresthemini.presentation.home.HomeScreen
import com.danielguillaumont.wheresthemini.presentation.parking.SaveParkingScreen

private object Routes {
    const val HOME = "home"
    const val SAVE_PARKING = "save_parking"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            HomeScreen(
                onParkedHereClick = {
                    navController.navigate(Routes.SAVE_PARKING)
                }
            )
        }

        composable(Routes.SAVE_PARKING) {
            SaveParkingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}