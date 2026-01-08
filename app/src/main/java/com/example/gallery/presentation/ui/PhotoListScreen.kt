package com.example.gallery.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gallery.domain.model.Photo
import com.example.gallery.presentation.intent.PhotoIntent
import com.example.gallery.presentation.viewmodel.PhotoViewModel

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(
    viewModel: PhotoViewModel,
    onPhotoClick: (Photo) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val photos = listState.photos.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(PhotoIntent.LoadPhotos)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Custom Gallery") })
        }
    ) { paddingValues ->
        if (photos.itemCount == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No photos found.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(photos.itemCount) { index ->
                    val photo = photos[index]
                    if (photo != null) {
                        PhotoItem(
                            photo = photo,
                            onClick = {
                                viewModel.handleIntent(PhotoIntent.ClickPhoto(photo))
                                onPhotoClick(photo)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItem(
    photo: Photo,
    onClick: () -> Unit
) {
    GlideImage(
        model = photo.uri,
        contentDescription = photo.name,
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}
