package com.example.eyeOnTheNews

import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.data.repository.NewsRepositoryImplementation
import com.example.eyeOnTheNews.data.repository.NewsResult
import com.example.eyeOnTheNews.data.repository.toEntity
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsDao
import com.example.eyeOnTheNews.data.source.remote.RemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NewsRepositoryUnitTests {

    @Mock
    private lateinit var mockNewsArticleDao: EyeOnTheNewsDao

    @Mock
    private lateinit var mockRemoteDataSource: RemoteDataSourceImpl

    private lateinit var newsRepository: NewsRepositoryImplementation

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        newsRepository = NewsRepositoryImplementation(mockNewsArticleDao, mockRemoteDataSource)
    }

    @Test
    fun testRefreshNewsArticles() = runBlocking {
        // Prepare the data
        val latestNewsArticles = NewsResult.success(listOf(
            NewsArticle(id = 1, author = "Author1", title = "Title1", description = "", url = "", source = "", image = "", category = "", language = "", country = "", published = "2022-03-01", saved = false),
            NewsArticle(id = 2, author = "Author2", title = "Title2", description = "", url = "", source = "", image = "", category = "", language = "", country = "", published = "2022-03-01", saved = false)
        ))

        // Mock the fetchLatestNewsArticles method to return the prepared data
        `when`(mockRemoteDataSource.getAllNews()).thenReturn(latestNewsArticles)

        // Call the method under test
        val notificationArticles = newsRepository.refreshNewsArticles()

        // Verify that fetchLatestNewsArticles was called
        verify(mockRemoteDataSource).getAllNews()

        // Verify that saveNewsArticle was called for each news article
        latestNewsArticles.data!!.forEach { verify(mockNewsArticleDao).insertNewsArticle(it.toEntity()) }

        // Verify that deleteUnsavedOldArticles was called
        verify(newsRepository).deleteUnsavedOldArticles()

        // Assert that the returned list of notification articles is correct
        assertEquals(latestNewsArticles.data, notificationArticles)
    }
}