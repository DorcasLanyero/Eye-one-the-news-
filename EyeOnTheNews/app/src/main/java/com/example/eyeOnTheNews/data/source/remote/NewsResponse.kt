package com.example.eyeOnTheNews.data.source.remote

import com.example.eyeOnTheNews.data.repository.NewsArticle

/**
 * The `NewsResponse` data class models the data returned from the response body of the API call.
 * It contains a `Pagination` object and a list of `NewsArticle` objects.
 *
 * @property pagination The `Pagination` object contains information about the pagination of the data.
 * @property data The list of `NewsArticle` objects contains the news articles returned from the API call.
 */
data class NewsResponse(
    val pagination: Pagination,
    val data: List<NewsArticle>
)

/**
 * The `Pagination` data class models the pagination information of the data returned from the API call.
 * It contains the limit, offset, count, and total of the data.
 *
 * @property limit The limit is the maximum number of data items returned per page.
 * @property offset The offset is the number of data items skipped from the start.
 * @property count The count is the number of data items returned in the current page.
 * @property total The total is the total number of data items available.
 */
data class Pagination(
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)