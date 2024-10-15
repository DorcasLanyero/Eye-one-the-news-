package com.example.eyeOnTheNews

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsDao
import com.example.eyeOnTheNews.data.source.local.NewsArticleEntity
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsRoomDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class NewsArticleDaoTest {

    private lateinit var db: EyeOnTheNewsRoomDB
    private lateinit var newsArticleDao: EyeOnTheNewsDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, EyeOnTheNewsRoomDB::class.java).build()
        newsArticleDao = db.inspectionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieveNewsArticle() = runBlocking {
        val newsArticle = NewsArticleEntity(
            author = "Author",
            title = "Title",
            description = "Description",
            url = "URL",
            source = "Source",
            image = "Image URL",
            category = "Category",
            language = "Language",
            country = "Country",
            published = "Published At"
        )
        val id = newsArticleDao.insertNewsArticle(newsArticle)
        val retrievedNewsArticle = newsArticleDao.getNewsArticle(id.toInt())
        val expectedNewsArticle = newsArticle.copy(id = id.toInt())
        assertEquals(expectedNewsArticle, retrievedNewsArticle)
    }

    @Test
    fun testUpdateNewsArticle() = runBlocking {
        val newsArticle = NewsArticleEntity(
            author = "Author",
            title = "Title",
            description = "Description",
            url = "URL",
            source = "Source",
            image = "Image URL",
            category = "Category",
            language = "Language",
            country = "Country",
            published = "Published At",
            saved = false
        )
        val id = newsArticleDao.insertNewsArticle(newsArticle)

        val updatedNewsArticle = newsArticle.copy(id = id.toInt(), saved = true)
        newsArticleDao.updateNewsArticle(updatedNewsArticle)

        val retrievedNewsArticle = newsArticleDao.getNewsArticle(id.toInt())
        assertEquals(updatedNewsArticle, retrievedNewsArticle)
    }


    @Test
    fun testFetchNewsArticlesByCategory() = runBlocking {
        // Insert a few NewsArticle objects with different categories
        val newsArticle1 = NewsArticleEntity(
            author = "Author1",
            title = "Title1",
            description = "Description1",
            url = "URL1",
            source = "Source1",
            image = "Image URL1",
            category = "Category1",
            language = "Language1",
            country = "Country1",
            published = "Published At1"
        )
        val newsArticle2 = NewsArticleEntity(
            author = "Author2",
            title = "Title2",
            description = "Description2",
            url = "URL2",
            source = "Source2",
            image = "Image URL2",
            category = "Category2",
            language = "Language2",
            country = "Country2",
            published = "Published At2"
        )
        newsArticleDao.insertNewsArticle(newsArticle1)
        newsArticleDao.insertNewsArticle(newsArticle2)

        // Fetch news articles by category
        val newsArticlesByCategory = newsArticleDao.getNewsArticlesByCategory("Category1").first()

        // Check if the returned list contains only articles with the specified category
        for (article in newsArticlesByCategory) {
            assertEquals("Category1", article.category)
        }
    }

    @Test
    fun testDeleteOldUnsavedNewsArticles() = runBlocking {
        val oldNewsArticle = NewsArticleEntity(
            author = "Old Author",
            title = "Old Title",
            description = "Old Description",
            url = "Old URL",
            source = "Old Source",
            image = "Old Image URL",
            category = "Old Category",
            language = "Old Language",
            country = "Old Country",
            published = "2022-01-01",
            saved = false
        )
        val newNewsArticleId = newsArticleDao.insertNewsArticle(oldNewsArticle)

        // Insert a NewsArticle that was published less than a month ago and is not saved
        val newNewsArticle = NewsArticleEntity(
            author = "New Author",
            title = "New Title",
            description = "New Description",
            url = "New URL",
            source = "New Source",
            image = "New Image URL",
            category = "New Category",
            language = "New Language",
            country = "New Country",
            published = "2024-10-10",
            saved = false
        )
        val newNewsArticleId_1 = newsArticleDao.insertNewsArticle(newNewsArticle)

        // Get the date a month ago
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val monthAgo = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        // Delete old unsaved news articles
         newsArticleDao.deleteUnsavedNewsArticle(newNewsArticleId.toInt())

        // Fetch all news articles
        val allNewsArticles = newsArticleDao.getAllNewsArticles().first()

        // Check if the old unsaved news article was deleted
        assertFalse(allNewsArticles.contains(oldNewsArticle.copy(id = newNewsArticleId.toInt())))

        // Check if the new unsaved news article was not deleted
        assertTrue(allNewsArticles.contains(newNewsArticle.copy(id = newNewsArticleId_1.toInt())))
    }
}