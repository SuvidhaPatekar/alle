package com.example.alle.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionRequester(
    contentResolver: ContentResolver, context: Context
) {

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
        ImageProcessingScreen(contentResolver = contentResolver, context = context)
    }

    if (storagePermissionState.allPermissionsGranted && !showImageProcessingScreen) {
        // Permission is already granted, ImageProcessingScreen
        showImageProcessingScreen = true
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