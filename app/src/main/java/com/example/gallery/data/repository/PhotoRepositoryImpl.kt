package com.example.gallery.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gallery.data.paging.PhotoPagingSource
import com.example.gallery.domain.model.Photo
import com.example.gallery.domain.repository.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PhotoRepository {

    override fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(context) }
        ).flow
    }
}
