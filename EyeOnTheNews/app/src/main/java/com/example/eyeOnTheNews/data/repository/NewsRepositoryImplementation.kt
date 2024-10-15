package com.example.eyeOnTheNews.data.repository

import android.util.Log
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsDao
import com.example.eyeOnTheNews.data.source.local.NewsArticleEntity
import com.example.eyeOnTheNews.data.source.remote.RemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class NewsRepositoryImplementation @Inject constructor(
    private val newsArticleDao: EyeOnTheNewsDao,
    private val remoteDataSource: RemoteDataSourceImpl
) : NewsRepository {
    private val TAG = "NewsRepository"

    override suspend fun fetchNewsArticle(id: Int): NewsArticle {
        Log.d(TAG, "fetchNewsArticle called with id: $id")
        return newsArticleDao.getNewsArticle(id).toModel()
    }

    override suspend fun fetchSavedNewsArticles(saved: Boolean): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchSavedNewsArticles called")
        return newsArticleDao.getSavedNewsArticles().map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun fetchNewsArticles(): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchSavedNewsArticles called")
        return newsArticleDao.getAllNewsArticles().map { newsArticleEntities ->
            newsArticleEntities.map { it.toModel() }
        }
    }

    override suspend fun fetchNewsArticlesByCategory(category: String): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchNewsArticlesByCategory called with category: $category")

        return newsArticleDao.getNewsArticlesByCategory(category)
            .map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun fetchNewsArticlesBySource(source: String): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchNewsArticlesBySource called with source: $source")
        return newsArticleDao.getNewsArticlesBySource(source)
            .map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun fetchNewsArticlesFromPublishedDate(publishedAt: String): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchNewsArticlesFromPublishedDate called with publishedAt: $publishedAt")
        return newsArticleDao.getNewsArticlesFromPublishedDate(publishedAt)
            .map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun saveNewsArticle(newsArticle: NewsArticle) {
        Log.d(TAG, "saveNewsArticle called with newsArticle: $newsArticle")
        newsArticleDao.insertNewsArticleIfNotExists(newsArticle.toEntity())
    }

    override suspend fun fetchLatestNewsArticles(): NewsResult {
        Log.d(TAG, "fetchLatestNewsArticles called")
        return remoteDataSource.getAllNews()
    }

    override suspend fun refreshNewsArticles(): List<NewsArticle> {
        Log.d(TAG, "refreshNewsArticles called")
        val notificationArticles = mutableListOf<NewsArticle>()
        val latestNewsArticles = fetchLatestNewsArticles()
        val calendar = Calendar.getInstance()
        val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val dayAgo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        if (latestNewsArticles.data != null) {
            for (article in latestNewsArticles.data) {
                val publishedAt = article.published
                if (article.category == "Breaking" && publishedAt > dayAgo) {
                    notificationArticles.add(article)
                }
                saveNewsArticle(article)
            }
        }
        //deleteUnsavedOldArticles()

        return notificationArticles
    }

    suspend fun deleteUnsavedOldArticles() {
        Log.d(TAG, "deleteUnsavedOldArticles called")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -30)
        val monthAgo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val unsavedNewsArticles = newsArticleDao.getUnsavedNewsArticles().first()

        val oldUnsavedNewsArticles = unsavedNewsArticles.filter { it.published > monthAgo }

        for (article in oldUnsavedNewsArticles) {
            newsArticleDao.deleteUnsavedNewsArticle(article.id)
        }
    }

    override suspend fun fetchNewsArticlesBySearchQuery(query: String): Flow<List<NewsArticle>> {
        Log.d(TAG, "fetchNewsArticlesBySearchQuery called with query: $query")

        return newsArticleDao.getNewsArticlesBySearchQuery(query)
            .map { it.map { entity -> entity.toModel() } }
    }

    override suspend fun updateNewsArticle(newsArticle: NewsArticle) {
        Log.d(TAG, "updateNewsArticle called with newsArticle: $newsArticle")
        val updatedArticle = newsArticle.copy(saved = !newsArticle.saved)
        newsArticleDao.updateNewsArticle(updatedArticle.toEntity())
    }
}

fun NewsArticle.toEntity(): NewsArticleEntity {
    return NewsArticleEntity(
        id = this.id,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        source = this.source,
        image = this.image,
        category = this.category,
        language = this.language,
        country = this.country,
        published = this.published,
        saved = this.saved
    )
}

fun NewsArticleEntity.toModel(): NewsArticle {
    return NewsArticle(
        id = this.id,
        author = this.author ?: "",
        title = this.title,
        description = this.description,
        url = this.url,
        source = this.source,
        image = this.image,
        category = this.category,
        language = this.language,
        country = this.country,
        published = this.published,
        saved = this.saved
    )
}