package com.example.gallery.presentation.util

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import com.example.gallery.domain.model.Photo
import java.io.InputStream

object ExifUtil {
    fun getExifMetadata(context: Context, photo: Photo): Map<String, String> {
        val metadata = mutableMapOf<String, String>()
        try {
            context.contentResolver.openInputStream(photo.uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                
                val tags = arrayOf(
                    ExifInterface.TAG_DATETIME,
                    ExifInterface.TAG_MAKE,
                    ExifInterface.TAG_MODEL,
                    ExifInterface.TAG_IMAGE_WIDTH,
                    ExifInterface.TAG_IMAGE_LENGTH,
                    ExifInterface.TAG_GPS_LATITUDE,
                    ExifInterface.TAG_GPS_LONGITUDE,
                    ExifInterface.TAG_EXPOSURE_TIME,
                    ExifInterface.TAG_F_NUMBER,
                    ExifInterface.TAG_ISO_SPEED_RATINGS
                )

                tags.forEach { tag ->
                    exif.getAttribute(tag)?.let { value ->
                        metadata[tag] = value
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        metadata["File Name"] = photo.name
        metadata["Size"] = "${photo.size / 1024} KB"
        metadata["Mime Type"] = photo.mimeType
        
        return metadata
    }
}
