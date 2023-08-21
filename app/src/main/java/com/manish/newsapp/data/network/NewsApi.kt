package com.manish.newsapp.data.network

import com.manish.newsapp.data.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String,
    ): Response<News>
}