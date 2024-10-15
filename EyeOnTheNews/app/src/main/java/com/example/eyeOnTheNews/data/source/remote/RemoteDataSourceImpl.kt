package com.example.eyeOnTheNews.data.source.remote

import android.util.Log
import com.example.eyeOnTheNews.data.repository.NewsResult
import javax.inject.Inject

/**
 * The `RemoteDataSourceBase` abstract class provides a base implementation for remote data sources.
 * It contains a method to handle API calls and their responses.
 *
 * @method getResult This method takes a suspend function that makes an API call and returns its response.
 * It checks if the response is successful and if the body is not null.
 * If the response is not successful or the body is null, it logs the error message and returns an error response.
 * If an exception occurs during the API call, it logs the exception message and returns an error response.
 *
 * @method error This private method takes an error message and returns an error response with the error message.
 */

class RemoteDataSourceImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RemoteDataSourceBase() {
    suspend fun getAllNews(): NewsResult {
        val response = getResult { remoteDataSource.getAllNews() }
        Log.d("RemoteDataSourceImpl", "Response: ${response.body()}")
        return if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                NewsResult.success(newsResponse.data)
            } ?: NewsResult.error("No news articles found")
        } else {
            NewsResult.error("Error fetching news articles: ${response.message()}")
        }
    }
}
