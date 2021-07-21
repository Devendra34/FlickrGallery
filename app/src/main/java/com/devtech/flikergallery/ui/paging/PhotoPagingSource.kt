package com.devtech.flikergallery.ui.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devtech.flikergallery.api.RetrofitInstance
import com.devtech.flikergallery.data.Photo
import com.devtech.flikergallery.repository.PhotosRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(private val query: String?) : PagingSource<Int, Photo>() {
    companion object {
        private const val TAG = "my_test"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val currentPage = params.key ?: 1
            val response = if (query.isNullOrEmpty()) {
                RetrofitInstance.api.getRecentPhotos(page = currentPage, perPage = params.loadSize)
            } else {
                RetrofitInstance.api.searchPhotos(
                    query = query.trim(),
                    perPage = params.loadSize,
                    page = currentPage
                )
            }
            val photos = response.photos
            val nextKey = if (photos.photo.size < params.loadSize) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                currentPage + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos.photo,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Log.d(TAG, "load error of type IOException: $e")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d(TAG, "load error of type HttpException: $e")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d(TAG, "load error of type Exception: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        Log.d(TAG, "getRefreshKey: calling")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}