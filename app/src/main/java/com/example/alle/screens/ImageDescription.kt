package com.example.alle.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberImagePainter
import com.example.alle.R
import com.example.alle.viewmodels.ImageViewModel

@Composable
fun ImageDescription(
    imageViewModel: ImageViewModel = viewModel(),
    backStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current
    val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
    LaunchedEffect(imageUri.isNotBlank()) {
        imageViewModel.getDescriptionAndLabels(Uri.parse(imageUri), context)
    }
    Scaffold(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp),

                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

                    Image(
                        painter = rememberImagePainter(Uri.parse(imageUri)),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .shadow(2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (imageViewModel.collections.value.size > 0) {
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
                                    .fillMaxHeight(0.7f)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = imageViewModel.collections.value[index],
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    if (imageViewModel.description.value.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.description),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start
                        )
                        BasicTextField(
                            value = TextFieldValue(text = imageViewModel.description.value),
                            onValueChange = {
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                }
            }

        }
    )
}