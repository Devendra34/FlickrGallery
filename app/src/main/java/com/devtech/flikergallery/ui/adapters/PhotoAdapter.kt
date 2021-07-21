package com.devtech.flikergallery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devtech.flikergallery.R
import com.devtech.flikergallery.data.Photo
import com.devtech.flikergallery.databinding.ImageItemCardBinding

class PhotoAdapter : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

    inner class PhotoViewHolder(val binding: ImageItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        with(holder.binding) {
            val photo = getItem(position) ?: return
            Glide.with(this.root.context).load(photo.url_s)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(imageView)
            tv1.text = photo.id
            tv2.text = photo.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        ImageItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}