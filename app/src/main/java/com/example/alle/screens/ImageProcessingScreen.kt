package com.example.alle.screens


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.alle.R
import com.example.alle.viewmodels.ImageViewModel
import com.example.alle.viewmodels.LoadingState

@Composable
fun ImageProcessingScreen(
    imageViewModel: ImageViewModel = viewModel(),
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
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .shadow(2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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
                                        imageViewModel.getDescriptionAndLabels(
                                            Uri.parse(imageViewModel.imageUris.value[index]),
                                            context
                                        )
                                    }
                            )
                        }
                    }

                    if(imageViewModel.collections.value.size > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.collections),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imageViewModel.collections.value.size) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = imageViewModel.collections.value[index],
                                    color = Color.White
                                )
                            }
                        }
                    }

                    if(imageViewModel.description.value.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.description),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start
                        )
                        Text(text = imageViewModel.description.value, color = Color.Black)
                    }
                }
            }
        )
    }
}