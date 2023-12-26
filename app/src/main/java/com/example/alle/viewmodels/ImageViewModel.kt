package com.example.alle.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alle.repository.ImageRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ImageViewModel(private val imageRepository: ImageRepository) : ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    var selectedUri: MutableState<Int> = mutableStateOf(0)
    var description: MutableState<String> = mutableStateOf("")
    var collections: MutableState<MutableList<String>> = mutableStateOf(mutableListOf())
    var imageUris: MutableState<MutableList<String>> = mutableStateOf(mutableListOf())

    @SuppressLint("Range")
    fun readScreenshots(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.emit(LoadingState.LOADING)
            imageUris.value = imageRepository.readScreenshots()
            withContext(Dispatchers.Main) {
                loadingState.emit(LoadingState.LOADED)
            }
        }
    }


    fun getDescriptionAndLabels(imageUri: Uri, context: Context) {
        // Iterate through the list of image URIs
        val result = StringBuilder()
        val image = InputImage.fromFilePath(context, imageUri)

        // Text Recognition
        val textRecognition = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        textRecognition.process(image).addOnSuccessListener {
            for (block in it.textBlocks) {
                for (line in block.lines) {
                    result.append(line.text).append("\n")
                }
            }
            description.value = result.toString()
        }

        //Get collections/labels for images
        collections.value.clear()
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image).addOnSuccessListener {
            collections.value.addAll(it.map {imageLabel ->
                imageLabel.text
            })
        }
    }
}

class LoadingState private constructor(val status: Status) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val IDLE = LoadingState(Status.IDLE)
        val LOADING = LoadingState(Status.RUNNING)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        IDLE,
    }
}


