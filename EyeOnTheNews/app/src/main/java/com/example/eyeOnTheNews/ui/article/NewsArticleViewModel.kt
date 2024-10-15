package com.example.eyeOnTheNews.ui.article

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
/**
 * The `NewsArticleViewModel` class is responsible for preparing and managing the data for `NewsArticleView`.
 * It extends the `ViewModel` class and is designed to store and manage UI-related data in a lifecycle conscious way.
 * It allows data to survive configuration changes such as screen rotations.
 *
 * @property newsRepository  Abstracts access to multiple data sources.
 * @property ioCoroutineDispatcher Is the CoroutineDispatcher that will be used for disk or network access.
 *
 * @property newsArticle The live data object containing a news article.
 *
 * @method fetchArticle fetches a news article by its id from the repository and updates the `newsArticle` live data object.
 * @method saveNewsArticle sets the `saved` property of the `newsArticle` object to true.
 * @method saveNewsArticleDB  saves the `newsArticle` object to the database.
 */

@HiltViewModel
class NewsArticleViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    val newsArticle = MutableLiveData<NewsArticle>()

    fun fetchArticle(articleId: Int) {
        viewModelScope.launch(ioCoroutineDispatcher) {
            try {
                val fetchedArticle = newsRepository.fetchNewsArticle(articleId)
                withContext(Dispatchers.Main) {
                    newsArticle.value = fetchedArticle
                    Log.d("fetchArticle in NewsArticleViewModel", "Yielded \"${fetchedArticle}\"")
                }
            } catch (e: Exception) {
                Log.e("fetchArticle in NewsArticleViewModel", "Yielded error: \"${e}\"")
            }
        }
    }

    fun saveNewsArticle() {
        newsArticle.value!!.saved = true
    }

    suspend fun saveNewsArticletoDB() {
        viewModelScope
        newsRepository.saveNewsArticle(newsArticle.value!!)
    }
}


