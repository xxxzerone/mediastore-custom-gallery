package com.example.gallery.data.paging

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gallery.domain.model.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoPagingSource(
    private val context: Context
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return withContext(Dispatchers.IO) {
            try {
                val position = params.key ?: 0
                val limit = params.loadSize
                val offset = position

                val photos = mutableListOf<Photo>()

                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.MIME_TYPE
                )

                val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    "${MediaStore.Images.Media.IS_PENDING} = 0"
                } else {
                    null
                }

                val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val queryArgs = Bundle().apply {
                        putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                        putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                        putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.Media.DATE_ADDED))
                        putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                        if (selection != null) {
                            putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                        }
                    }
                    context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        queryArgs,
                        null
                    )
                } else {
                    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset"
                    context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        null,
                        sortOrder
                    )
                }

                cursor?.use { c ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                    val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val dateAdded = cursor.getLong(dateAddedColumn)
                        val size = cursor.getLong(sizeColumn)
                        val mimeType = cursor.getString(mimeTypeColumn)
                        val contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )

                        photos.add(
                            Photo(
                                id = id,
                                uri = contentUri,
                                name = name,
                                dateAdded = dateAdded,
                                size = size,
                                mimeType = mimeType
                            )
                        )
                    }
                }

                LoadResult.Page(
                    data = photos,
                    prevKey = if (position == 0) null else position - limit,
                    nextKey = if (photos.isEmpty()) null else position + limit
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}
