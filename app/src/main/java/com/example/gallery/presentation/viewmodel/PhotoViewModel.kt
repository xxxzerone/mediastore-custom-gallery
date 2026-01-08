package com.example.gallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.gallery.domain.usecase.GetPhotosUseCase
import com.example.gallery.presentation.intent.PhotoIntent
import com.example.gallery.presentation.state.PhotoDetailState
import com.example.gallery.presentation.state.PhotoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow(PhotoListState())
    val listState: StateFlow<PhotoListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(PhotoDetailState())
    val detailState: StateFlow<PhotoDetailState> = _detailState.asStateFlow()

    fun handleIntent(intent: PhotoIntent) {
        when (intent) {
            is PhotoIntent.LoadPhotos -> loadPhotos()
            is PhotoIntent.ClickPhoto -> {
                _detailState.update { it.copy(selectedPhoto = intent.photo) }
            }
            is PhotoIntent.SharePhoto -> {
                // To be handled in UI or through a side effect
            }
        }
    }

    private fun loadPhotos() {
        if (_listState.value.photos != kotlinx.coroutines.flow.emptyFlow<androidx.paging.PagingData<com.example.gallery.domain.model.Photo>>()) {
            // Already initialized, but we might want to refresh. 
            // For now, let's just avoid re-initializing the entire flow if not needed.
        }

        val photoFlow = getPhotosUseCase()
            .cachedIn(viewModelScope)
        
        _listState.update { it.copy(photos = photoFlow) }
    }
}
