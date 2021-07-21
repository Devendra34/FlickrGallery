package com.devtech.flikergallery.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.devtech.flikergallery.ui.paging.PhotoPagingSource

class PhotosRepository {

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    fun getSearchResultStream(query: String) = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { PhotoPagingSource(query) }
    ).flow
}