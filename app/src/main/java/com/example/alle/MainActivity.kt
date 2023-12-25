package com.example.alle

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.alle.ui.theme.AlleTheme
import com.google.accompanist.permissions.*
import java.util.Collections.emptyList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StoragePermissionRequester()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionRequester() {
    var storagePermissionState: MultiplePermissionsState? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        storagePermissionState = rememberMultiplePermissionsState(mutableStateListOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        storagePermissionState = rememberMultiplePermissionsState(mutableStateListOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))

    } else {
        storagePermissionState = rememberMultiplePermissionsState(mutableStateListOf(READ_EXTERNAL_STORAGE))
    }

    var showImageProcessingScreen by remember { mutableStateOf(false) }

    if(showImageProcessingScreen) {
        showImageProcessingScreen = !showImageProcessingScreen
        ImageProcessingScreen()
    }

    if (storagePermissionState.allPermissionsGranted) {
        // Permission is already granted, proceed with your logic
        ImageProcessingScreen()
    }
    // Observe the permission state for changes
    LaunchedEffect(storagePermissionState) {
        when {
            storagePermissionState.allPermissionsGranted -> showImageProcessingScreen = true
            else ->  storagePermissionState.launchMultiplePermissionRequest()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ImageProcessingScreen() {
    val imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Logic to sync and process images
    //val imageProcessingResult by remember { viewModel().syncAndProcessImages(imageUris).collectAsState() }
    val selectedUri = ""
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Image Processing") },
                actions = {
                    // Refresh button to sync and process images
                    IconButton(onClick = {
                        // Trigger image synchronization and processing
                        // This is where you would load images from the gallery
                    }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(Color.Gray)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(selectedUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp, 100.dp)
                            .shadow(4.dp)
                    )
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUris.size) { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp, 100.dp)
                                .shadow(4.dp)
                        )
                    }
                }
            }
        }
    )
}



