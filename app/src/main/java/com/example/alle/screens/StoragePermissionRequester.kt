package com.example.alle.screens

import android.Manifest
import android.annotation.SuppressLint

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alle.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionRequester(navController: NavController) {

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
        navController.navigate("imageScreen")
        //ImageProcessingScreen(navController = navController)
    }

    if (storagePermissionState.allPermissionsGranted) {
        // Permission is already granted, ImageProcessingScreen
        showImageProcessingScreen = true
    } else {
        if (storagePermissionState.shouldShowRationale) {
            //Grant permission button'
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                0.2f
                            )
                        ), // Change the color here,
                        content = {
                            Text(
                                text = stringResource(id = R.string.grant_storage_permission),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.End,
                            )
                        },
                        onClick = {
                            storagePermissionState.launchMultiplePermissionRequest()
                        }

                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please open setting and give storage permission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

            }
        }
    }

    // Observe the permission state for changes
    LaunchedEffect(!showImageProcessingScreen) {
        when {
            storagePermissionState.allPermissionsGranted && !showImageProcessingScreen -> {
                showImageProcessingScreen = true
            }

            storagePermissionState.shouldShowRationale -> {

            }

            else -> {
                storagePermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}
