package com.example.alle.screens

import android.Manifest
import android.annotation.SuppressLint

import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.alle.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionRequester() {

    val storagePermissionState: MultiplePermissionsState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            rememberMultiplePermissionsState(
                mutableStateListOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberMultiplePermissionsState(
                mutableStateListOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            rememberMultiplePermissionsState(mutableStateListOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }

    var showImageProcessingScreen by remember { mutableStateOf(false) }

    if (showImageProcessingScreen) {
        ImageProcessingScreen()
    }

    if (storagePermissionState.allPermissionsGranted) {
        // Permission is already granted, ImageProcessingScreen
        showImageProcessingScreen = true
    } else {
        //Grant permission button
    }

    // Observe the permission state for changes
    LaunchedEffect(!showImageProcessingScreen) {
        when {
            storagePermissionState.allPermissionsGranted && !showImageProcessingScreen -> {
                showImageProcessingScreen = true
            }

            else -> {
                storagePermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}
