package com.example.alle

import android.Manifest.permission.*
import android.content.ContentResolver
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.alle.viewmodels.ImageViewModel
import com.google.accompanist.permissions.*

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
                    Log.d("setContent", "inside setContent")
                    StoragePermissionRequester(contentResolver)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionRequester(
    contentResolver: ContentResolver
) {

    val storagePermissionState: MultiplePermissionsState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            rememberMultiplePermissionsState(
                mutableStateListOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VIDEO,
                    READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberMultiplePermissionsState(
                mutableStateListOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VIDEO
                )
            )
        } else {
            rememberMultiplePermissionsState(mutableStateListOf(READ_EXTERNAL_STORAGE))
        }

    var showImageProcessingScreen by remember { mutableStateOf(false) }

    if (showImageProcessingScreen) {
        Log.d(
            "ImageProcessingScreen",
            "Inside imageProcessinf screen showImageProcessingScreen"
        )
        //showImageProcessingScreen = !showImageProcessingScreen
        ImageProcessingScreen(contentResolver)
    }

    if (storagePermissionState.allPermissionsGranted && !showImageProcessingScreen) {
        Log.d("ImageProcessingScreen", "Inside imageProcessinf screen allPermissionsGranted")
        // Permission is already granted, proceed with your logic
       showImageProcessingScreen = true
    }

    // Observe the permission state for changes
    LaunchedEffect(!showImageProcessingScreen) {
        when {
            storagePermissionState.allPermissionsGranted && !showImageProcessingScreen -> {
                Log.d("ImageProcessingScreen", "Inside imageProcessinf screen allPermissionsGranted")
                showImageProcessingScreen = true
            }
            else -> {
                storagePermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ImageProcessingScreen(
    contentResolver: ContentResolver,
    viewModel: ImageViewModel = ImageViewModel()
) {
    val imageUris: List<String> = viewModel.readScreenshots(contentResolver)

    // Logic to sync and process images
    //val imageProcessingResult by remember { viewModel().syncAndProcessImages(imageUris).collectAsState() }
    val selectedUri = imageUris[0]
    Log.d(
        "ImageProcessingScreen",
        "Inside imageProcessinf screen imageUris = ${imageUris.size} andselectedUri = $selectedUri "
    )
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
                        .fillMaxHeight(0.7f)
                        .background(Color.Gray)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberImagePainter("file://$selectedUri"),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
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
                    items(imageUris.size) { index ->
                        Log.d("lazy","Inside items uri = ${imageUris[index]}")
                        Image(
                            painter = rememberImagePainter("file://${imageUris[index]}"),
                            contentDescription = null,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .shadow(4.dp)
                        )
                    }
                }
            }
        }
    )
}




