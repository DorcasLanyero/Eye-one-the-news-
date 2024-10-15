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
package com.example.eyeOnTheNews.ui.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class HomeViewState(
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val newsArticles: MutableStateFlow<List<NewsArticle>> = MutableStateFlow(emptyList()),
    val notifications: MutableStateFlow<List<NewsArticle>> = MutableStateFlow(emptyList()),
    val userProfile: MutableStateFlow<String> = MutableStateFlow(""),
    val savedNewsArticles: MutableStateFlow<List<NewsArticle>> = MutableStateFlow(emptyList()),
    val categories: MutableStateFlow<List<String>> = MutableStateFlow(listOf("business", "sports", "entertainment", "general", "health", "science", "technology")),
    val carouselArticles:MutableStateFlow<List<NewsArticle>> = MutableStateFlow(emptyList())
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val selectedArticle = MutableLiveData<NewsArticle>()
    private val selectedCategory = MutableLiveData<String>()


    private val _homeUiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch(ioCoroutineDispatcher) {
            Log.d("HomeViewModel", "Initializing ViewModel")
            fetchNewsArticles()
            refreshNewsArticles()
            Log.d("HomeViewModel", "ViewModel initialized")
        }
    }

    fun selectArticle(article: NewsArticle) {
        selectedArticle.value = article
    }
    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun refreshNewsArticles() {
        viewModelScope.launch {
            newsRepository.refreshNewsArticles()
        }
    }

    private fun fetchNewsArticles() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching news articles")
            _homeUiState.value = _homeUiState.value.copy(isLoading = MutableStateFlow(true))

            newsRepository.fetchNewsArticles().collect { newsArticles ->
                _homeUiState.value = _homeUiState.value.copy(
                    newsArticles = MutableStateFlow(newsArticles),
                    isLoading = MutableStateFlow(false)
                )
                Log.d("HomeViewModel", "Fetched ${newsArticles.size} news articles")

                fetchRecentArticlesFromEachCategory(_homeUiState.value.categories.value)
            }
        }
    }

    fun fetchRecentArticlesFromEachCategory(categories: List<String>) {
        viewModelScope.launch(ioCoroutineDispatcher) {
            Log.d("HomeViewModel", "Fetching recent articles from each category")
            val carouselArticles = mutableListOf<NewsArticle>()

            for (category in categories) {
                val categoryArticle = _homeUiState.value.newsArticles.value.firstOrNull { it.category == category}

                // Log the category of the categoryArticle
                if (categoryArticle != null) {
                    Log.d("HomeViewModel", "Found article with category: ${categoryArticle.category}")
                    carouselArticles.add(categoryArticle)
                } else {
                    Log.d("HomeViewModel", "No article found for category: $category")
                }
            }

            withContext(Dispatchers.Main) {
                _homeUiState.value = _homeUiState.value.copy(carouselArticles = MutableStateFlow(carouselArticles))
                Log.d("HomeViewModel", "Fetched ${carouselArticles.size} carousel articles")
            }
        }
    }

    fun fetchSavedNewsArticles() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching saved news articles")
            newsRepository.fetchNewsArticles().collect { newsArticles ->
                _homeUiState.value = _homeUiState.value.copy(
                    savedNewsArticles = MutableStateFlow(newsArticles)
                )
                Log.d("HomeViewModel", "Fetched ${newsArticles.size} saved news articles")
            }
        }
    }


    /*
        fun fetchNewsArticlesBySource(source: String) {
            viewModelScope.launch {
                val newsArticlesBySource = newsRepository.fetchNewsArticlesBySource(source)
                viewState.newsArticles.value = newsArticlesBySource
            }
        }
     */

    /*
        fun fetchNewsArticlesFromPublishedDate(publishedAt: String) {
            viewModelScope.launch {
                val newsArticlesFromPublishedDate = newsRepository.fetchNewsArticlesFromPublishedDate(publishedAt)
                viewState.newsArticles.value = newsArticlesFromPublishedDate
            }
        }

     */

    /*
        fun fetchNewsArticlesBySearchQuery(query: String) {
            viewModelScope.launch {
                val newsArticlesBySearchQuery = newsRepository.fetchNewsArticlesBySearchQuery(query)
                viewState.newsArticles.value = newsArticlesBySearchQuery
            }
        }

     */

    fun saveNewsArticle(newsArticle: NewsArticle) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Saving news article")
            newsRepository.updateNewsArticle(newsArticle)
            Log.d("HomeViewModel", "News article saved")

        }
    }
}