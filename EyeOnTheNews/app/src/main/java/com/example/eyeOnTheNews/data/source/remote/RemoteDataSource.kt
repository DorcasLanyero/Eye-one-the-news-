package com.example.eyeOnTheNews.data.source.remote

import retrofit2.Response
import retrofit2.http.GET

interface RemoteDataSource {
    @GET("news?access_key=3e14851337dbd6646a91ecc6688a27ca&sources=CNN,bbc&categories=business,sports,entertainment,general,health,science,technology&languages=en")
    suspend fun getAllNews(): Response<NewsResponse>
}