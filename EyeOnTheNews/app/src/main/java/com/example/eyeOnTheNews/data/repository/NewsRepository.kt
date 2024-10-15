/**
 * This file contains the `NewsRepository` interface which defines the methods for fetching,
 * saving, and updating news articles.
 * It also contains the `NewsArticle` data class which represents a news article.
 */

package com.example.eyeOnTheNews.data.repository

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    //fetches a news article by its id
    suspend fun fetchNewsArticle(id: Int): NewsArticle
    //fetches all saved news articles
    suspend fun fetchSavedNewsArticles(saved: Boolean): Flow<List<NewsArticle>>
    //fetches all news articles
    suspend fun fetchNewsArticles(): Flow<List<NewsArticle>>
    //fetches news articles by category
    suspend fun fetchNewsArticlesByCategory(category: String): Flow<List<NewsArticle>>
    //fetches news articles by source
    suspend fun fetchNewsArticlesBySource(source: String): Flow<List<NewsArticle>>
    //fetches news articles from a published date
    suspend fun fetchNewsArticlesFromPublishedDate(publishedAt: String): Flow<List<NewsArticle>>
    //saves a news article
    suspend fun saveNewsArticle(newsArticle: NewsArticle)
    //fetches the latest news articles
    suspend fun fetchLatestNewsArticles(): NewsResult
    //refreshes the news articles, deleting any articles that are older than a month and fetching new articles from the media stack api
    suspend fun refreshNewsArticles(): List<NewsArticle>
    //fetches news articles by search query
    suspend fun fetchNewsArticlesBySearchQuery(query: String): Flow<List<NewsArticle>>
    //updates a news article
    suspend fun updateNewsArticle(newsArticle: NewsArticle)
}

data class NewsArticle(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("source") val source: String,
    @SerializedName("image") val image: String,
    @SerializedName("category") val category: String,
    @SerializedName("language") val language: String,
    @SerializedName("country") val country: String,
    @SerializedName("published_at") val published: String,
    var saved: Boolean = false
)

