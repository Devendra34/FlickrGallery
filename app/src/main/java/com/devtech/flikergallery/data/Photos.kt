package com.devtech.flikergallery.data

data class Photos(
    val page: Int,
    val pages: Int,
    val photo: List<Photo>,
    val total: Int
)