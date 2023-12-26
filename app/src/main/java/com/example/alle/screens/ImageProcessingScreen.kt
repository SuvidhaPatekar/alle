package com.example.alle.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.alle.R
import com.example.alle.viewmodels.ImageViewModel
import com.example.alle.viewmodels.LoadingState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ImageProcessingScreen(
    imageViewModel: ImageViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    imageViewModel.readScreenshots(context)
    val loadingState = imageViewModel.loadingState.collectAsState()

    if (loadingState.value == LoadingState.LOADING) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
            )
        }
    } else if(loadingState.value == LoadingState.LOADED) {
        Scaffold(
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(it),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = rememberImagePainter(imageViewModel.imageUris.value[imageViewModel.selectedUri.value]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .fillMaxWidth()
                            .shadow(2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxHeight(0.3f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(imageViewModel.imageUris.value.size) { index ->
                            Image(
                                painter = rememberImagePainter(imageViewModel.imageUris.value[index]),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clickable {
                                        imageViewModel.selectedUri.value = index
                                    }
                            )
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.see_details),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.clickable {
                            val encodedUrl = URLEncoder.encode(imageViewModel.imageUris.value[imageViewModel.selectedUri.value], StandardCharsets.UTF_8.toString())
                            navController.navigate("imageDescription/$encodedUrl")
                        }.align(Alignment.End).padding(8.dp)
                    )
                }
            }
        )
    }
}