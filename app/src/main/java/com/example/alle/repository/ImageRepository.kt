package com.example.alle.repository

import android.content.ContentResolver

interface ImageRepository {
   suspend fun readScreenshots() :MutableList<String>
}