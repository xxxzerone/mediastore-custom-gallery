package com.example.gallery.domain.repository

import androidx.paging.PagingData
import com.example.gallery.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(): Flow<PagingData<Photo>>
}
