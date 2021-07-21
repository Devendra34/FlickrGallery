package com.devtech.flikergallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devtech.flikergallery.repository.PhotosRepository

class PhotosViewModelProviderFactory(
    private val photosRepository: PhotosRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotosViewModel(photosRepository) as T
    }
}