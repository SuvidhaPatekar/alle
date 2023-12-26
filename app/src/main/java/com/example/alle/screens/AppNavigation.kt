package com.example.alle.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "permission"
    ) {
        composable("permission") {
            StoragePermissionRequester(navController)
        }
        composable("imageScreen") {
            ImageProcessingScreen(navController = navController)
        }
        composable("imageDescription/{imageUri}") { backStackEntry ->
            ImageDescription(backStackEntry = backStackEntry)
        }
    }
}