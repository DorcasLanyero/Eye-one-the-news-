package com.example.eyeOnTheNews.data.source.remote

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

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

abstract class RemoteDataSourceBase {
    open suspend fun <T> getResult(call: suspend () -> Response<T>): Response<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Response.success(body)
            }

            Log.d("APIRequestResponse", "Error: ${response.message()}")
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            Log.d("APIRequestResponse", "Error: ${e.message}")
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): Response<T> {
        //ToDo:Log
        val errorBody: ResponseBody = errorMessage.toResponseBody("text/plain".toMediaTypeOrNull())
        return Response.error(400, errorBody)
    }
}