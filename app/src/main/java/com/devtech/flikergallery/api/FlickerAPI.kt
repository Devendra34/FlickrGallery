package com.devtech.flikergallery.api

import com.devtech.flikergallery.data.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerAPI {

    @GET("?method=flickr.photos.getRecent&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s")
    suspend fun getRecentPhotos(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): PhotosResponse


    @GET("?method=flickr.photos.search&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s")
    suspend fun searchPhotos(
        @Query("text") query: String = "cat",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): PhotosResponse
}