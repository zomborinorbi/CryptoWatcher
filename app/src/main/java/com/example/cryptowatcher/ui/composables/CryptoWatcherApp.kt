package com.example.cryptowatcher.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "list") {
        composable(route = "list") {
            CryptoListScreen(
                navigateToDetails = {
                    navController.navigate(
                        route = "details/${it.id}"
                    )
                }
            )
        }
        composable(
            route = "details/{cryptoId}",
            arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
        ) {
            CryptoDetailsScreen(
                navigateBackToHomeScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}
