package com.devtech.flikergallery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devtech.flikergallery.databinding.LoadingErrorItemCardBinding

class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {

    inner class PagingLoadStateViewHolder(val binding: LoadingErrorItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.retryBtn.setOnClickListener { retry() }
            }
        }

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        with(holder.binding) {
            if (loadState is LoadState.Error) {
                errorText.text = loadState.error.localizedMessage
            }
            progressCircular.isVisible = loadState is LoadState.Loading
            retryBtn.isVisible = loadState is LoadState.Error
            errorText.isVisible = loadState is LoadState.Error

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = PagingLoadStateViewHolder(
        LoadingErrorItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}