package com.example.alle.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.use
import java.util.*


class ImageViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    var selectedUri: MutableState<Int> = mutableStateOf(0)
    var description: MutableState<String> = mutableStateOf("")
    var collections: MutableState<MutableList<String>> = mutableStateOf(mutableListOf())
    var imageUris: MutableState<MutableList<String>> = mutableStateOf(mutableListOf())
    @SuppressLint("Range")
    fun readScreenshots(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.emit(LoadingState.LOADING)
            val externalImagesUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(MediaStore.Images.Media._ID)
            val selection = "${MediaStore.Images.Media.DATA} like ?"
            val selectionArgs = arrayOf("%Screenshots%")
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val cursor = context.contentResolver.query(
                externalImagesUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )


            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val imageId: Long = it.getLong(it.getColumnIndex(MediaStore.Images.Media._ID))
                        val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imageId
                        )
                        imageUris.value.add(contentUri.toString())
                    } while (it.moveToNext())
                }
            }
            withContext(Dispatchers.Main) {
                loadingState.emit(LoadingState.LOADED)
                getDescriptionAndLabels(imageUri = Uri.parse(imageUris.value[0]), context = context)
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
                    result.append(line.text)
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
        }.addOnCanceledListener {
            collections.value.addAll(listOf())
        }
    }
}

class LoadingState private constructor(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val IDLE = LoadingState(Status.IDLE)
        val LOADING = LoadingState(Status.RUNNING)
        fun error(msg: String?) = LoadingState(Status.FAILED, msg)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED,
        IDLE,
    }
}


