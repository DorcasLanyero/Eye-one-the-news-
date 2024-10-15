/**
 * The `NewsResult` data class is used to handle the results of network requests or database operations.
 * It contains the status of the operation, an error message if any, and the data returned from the operation.
 *
 * @property resultStatus The status of the operation. It can be LOADING, ERROR, or SUCCESS.
 * @property errorMessage The error message if the operation fails. It is null if the operation is successful.
 * @property data The data returned from the operation. It is a list of `NewsArticle` objects. It is null if the operation fails.
 *
 * The `ResultStatus` enum class is used to represent the status of the operation.
 *
 * The companion object contains factory methods to create instances of `NewsResult` for different scenarios:
 * - loading(): Creates a `NewsResult` instance representing a loading state.
 * - error(exception: String): Creates a `NewsResult` instance representing an error state with the provided exception message.
 * - success(data: List<NewsArticle>): Creates a `NewsResult` instance representing a successful state with the provided data.
 */

package com.example.eyeOnTheNews.data.repository

data class NewsResult(
    val resultStatus: ResultStatus,
    val errorMessage: String?,
    val data: List<NewsArticle>?
) {

    enum class ResultStatus {
        LOADING,
        ERROR,
        SUCCESS,
    }

    companion object {
        fun loading(): NewsResult {
            return NewsResult(ResultStatus.LOADING, null, null)
        }

        fun error(exception: String): NewsResult {
            return NewsResult(ResultStatus.ERROR, exception, null)
        }

        fun success(data: List<NewsArticle>): NewsResult {
            return NewsResult(ResultStatus.SUCCESS, null, data)
        }
    }
}