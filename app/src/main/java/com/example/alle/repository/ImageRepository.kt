package com.example.alle.repository

import android.content.ContentResolver

interface ImageRepository {
   suspend fun readScreenshots(contentResolver: ContentResolver) :MutableList<String>
}