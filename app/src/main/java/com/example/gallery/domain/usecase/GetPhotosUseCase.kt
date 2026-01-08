package com.example.gallery.domain.usecase

import androidx.paging.PagingData
import com.example.gallery.domain.model.Photo
import com.example.gallery.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    operator fun invoke(): Flow<PagingData<Photo>> {
        return repository.getPhotos()
    }
}
