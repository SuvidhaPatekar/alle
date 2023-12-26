package com.example.alle.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alle.screens.ImageDescription
import com.example.alle.screens.ImageProcessingScreen
import com.example.alle.screens.StoragePermissionRequester

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