package com.example.alle.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import okio.use

class ImageRepositoryImpl : ImageRepository{
    @SuppressLint("Range")
    override suspend fun readScreenshots(contentResolver: ContentResolver): MutableList<String> {
        val imageUris: MutableList<String> = mutableListOf()
        val externalImagesUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DATA} like ?"
        val selectionArgs = arrayOf("%Screenshots%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = contentResolver.query(
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
                    imageUris.add(contentUri.toString())
                } while (it.moveToNext())
            }
        }
        return imageUris
    }
}