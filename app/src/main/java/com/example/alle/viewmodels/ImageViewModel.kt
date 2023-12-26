package com.example.alle.viewmodels

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import okio.use


class ImageViewModel : ViewModel() {

    var selectedUri: MutableState<Int> = mutableStateOf(0)

    @SuppressLint("Range")
    fun readScreenshots(contentResolver: ContentResolver) : List<String> {
        // Use ContentResolver to query external images
        Log.d("viiew model", "Inside readScreenshots")
        val externalImagesUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.DATA} like ?"
        val selectionArgs = arrayOf("%Screenshots%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor: Cursor? = contentResolver.query(externalImagesUri, projection, selection, selectionArgs, sortOrder)
        val imageUris: MutableList<String> = mutableStateListOf()

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    // Retrieve image data
                    val imageId: Long = it.getLong(it.getColumnIndex(MediaStore.Images.Media._ID))
                    val imagePath: String = it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA))
                    val imageUri = Uri.parse(imagePath)
                    imageUris.add(imagePath)
                    // Handle the image data as needed
                    // ...
                } while (it.moveToNext())
            }
        }
        return imageUris
    }
}

