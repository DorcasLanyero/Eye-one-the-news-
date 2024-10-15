package com.example.eyeOnTheNews.data.source.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Entity
@Dao
interface EyeOnTheNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsArticle(newsArticle: NewsArticleEntity): Long

    @Query("SELECT * FROM news_articles WHERE id = :id")
    suspend fun getNewsArticle(id: Int): NewsArticleEntity

    @Query("SELECT * FROM news_articles")
    fun getAllNewsArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE saved = 1")
    fun getSavedNewsArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE saved = 0")
    fun getUnsavedNewsArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE category = :category")
    fun getNewsArticlesByCategory(category: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE source = :source")
    fun getNewsArticlesBySource(source: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE published = :publishedAt")
    fun getNewsArticlesFromPublishedDate(publishedAt: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE title LIKE '%' || :query || '%'")
    fun getNewsArticlesBySearchQuery(query: String): Flow<List<NewsArticleEntity>>

    @Update
    suspend fun updateNewsArticle(newsArticle: NewsArticleEntity)

    @Query("DELETE FROM news_articles WHERE id = :id AND saved = 0")
    suspend fun deleteUnsavedNewsArticle(id: Int)

    @Query("SELECT * FROM news_articles WHERE published = :published")
    suspend fun getNewsArticleByDate(published: String): NewsArticleEntity?

    @Transaction
    suspend fun insertNewsArticleIfNotExists(newsArticle: NewsArticleEntity) {
        val existingArticle = getNewsArticleByDate(newsArticle.published)
        if (existingArticle == null) {
            insertNewsArticle(newsArticle)
        }
    }
}
