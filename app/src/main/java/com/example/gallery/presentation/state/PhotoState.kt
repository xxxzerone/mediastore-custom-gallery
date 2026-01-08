package com.example.gallery.presentation.state

import androidx.paging.PagingData
import com.example.gallery.domain.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PhotoListState(
    val photos: Flow<PagingData<Photo>> = emptyFlow()
)

data class PhotoDetailState(
    val selectedPhoto: Photo? = null,
    val exifData: Map<String, String> = emptyMap()
)
