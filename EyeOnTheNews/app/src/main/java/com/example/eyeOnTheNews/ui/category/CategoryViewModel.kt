/**
 * The `CategoryViewModel` class is responsible for preparing and managing the data for `CategoryView`.
 * It extends the `ViewModel` class and is designed to store and manage UI-related data in a lifecycle conscious way.
 * It allows data to survive configuration changes such as screen rotations.
 *
 * @property newsRepository Abstracts access to multiple data sources.
 * @property ioCoroutineDispatcher Is the CoroutineDispatcher that will be used for disk or network access.
 *
 * @property newsArticlesByCategory The state flow object containing a list of news articles by category.
 *
 * @method fetchCategoryArticles fetches news articles by category from the repository and updates the `newsArticlesByCategory` state flow object.
 */
package com.example.eyeOnTheNews.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _newsArticlesByCategory = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticlesByCategory: StateFlow<List<NewsArticle>> = _newsArticlesByCategory


    fun fetchCategoryArticles(selectedCategory: String){
        viewModelScope.launch(ioCoroutineDispatcher) {
            try {
                newsRepository.fetchNewsArticlesByCategory(selectedCategory).collect { fetchedArticles ->
                    withContext(Dispatchers.Main) {
                        _newsArticlesByCategory.value = fetchedArticles
                        Log.d("fetchArticle in NewsArticleViewModel", "Yielded \"${fetchedArticles}\"")
                    }
                }
            } catch (e: Exception) {
                Log.e("fetchCategoryArticles in CategoryViewModel", "Yielded error: \"${e}\"")
            }
        }
    }

}