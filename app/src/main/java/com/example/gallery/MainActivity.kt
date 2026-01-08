package com.example.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gallery.presentation.ui.PhotoDetailScreen
import com.example.gallery.presentation.ui.PhotoListScreen
import com.example.gallery.presentation.viewmodel.PhotoViewModel
import com.example.gallery.ui.theme.MediaStoreGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.gallery.presentation.intent.PhotoIntent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: PhotoViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.handleIntent(PhotoIntent.LoadPhotos)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        checkAndRequestPermissions()

        setContent {
            MediaStoreGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GalleryApp(viewModel)
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.handleIntent(PhotoIntent.LoadPhotos)
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun sharePhoto(uri: android.net.Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share Photo"))
    }

    @Composable
    fun GalleryApp(viewModel: PhotoViewModel) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                PhotoListScreen(
                    viewModel = viewModel,
                    onPhotoClick = { photo ->
                        navController.navigate("detail")
                    }
                )
            }
            composable("detail") {
                PhotoDetailScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onShare = {
                        viewModel.detailState.value.selectedPhoto?.let { photo ->
                            sharePhoto(photo.uri)
                        }
                    }
                )
            }
        }
    }
}
