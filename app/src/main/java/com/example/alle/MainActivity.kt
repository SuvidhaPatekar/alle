package com.example.alle

import android.net.Uri
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
import java.util.Collections.emptyList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ImageProcessingScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageProcessingScreen() {
    val imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Logic to sync and process images
    //val imageProcessingResult by remember { viewModel().syncAndProcessImages(imageUris).collectAsState() }
    val imageProcessingResult  = emptyList<Uri>()
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
                // Display the large view (height 0.8f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(16.dp)
                ) {
                    // You can add content inside this Box as needed
                    Text("Large View Content Goes Here")
                }

                // Display the LazyRow (height 0.2f)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
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

