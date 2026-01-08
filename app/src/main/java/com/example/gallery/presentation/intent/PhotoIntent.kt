package com.example.gallery.presentation.intent

import com.example.gallery.domain.model.Photo

sealed class PhotoIntent {
    object LoadPhotos : PhotoIntent()
    data class ClickPhoto(val photo: Photo) : PhotoIntent()
    data class SharePhoto(val photo: Photo) : PhotoIntent()
}
