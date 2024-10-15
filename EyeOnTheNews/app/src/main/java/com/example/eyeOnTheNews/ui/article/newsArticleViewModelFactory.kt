/**
 * The `NewsArticleViewModelFactory` class is responsible for creating instances of `NewsArticleViewModel`.
 * It implements the `ViewModelProvider.Factory` interface which is used to instantiate ViewModels with complex constructors.
 *
 * @property newsArticle The `NewsArticle` object that will be passed to the `NewsArticleViewModel`.
 * @property newsRepository The `NewsRepository` instance that will be passed to the `NewsArticleViewModel`.
 * @property ioCoroutineDispatcher The `CoroutineDispatcher` that will be used for disk or network access in the `NewsArticleViewModel`.
 *
 * @method create This method is overridden to create an instance of `NewsArticleViewModel`. It checks if the `modelClass` is assignable from `NewsArticleViewModel` and if so, creates a new instance of `NewsArticleViewModel`. If the `modelClass` is not assignable from `NewsArticleViewModel`, it throws an `IllegalArgumentException`.
 */

package com.example.eyeOnTheNews.ui.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher

class NewsArticleViewModelFactory(
    private val newsArticle: NewsArticle,
    private val newsRepository: NewsRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            if (modelClass.isAssignableFrom(NewsArticleViewModel::class.java)) {
                NewsArticleViewModel( newsRepository, ioCoroutineDispatcher) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        } catch (e: IllegalArgumentException) {
            Log.e("NewsArticleViewModelFactory", "Error creating ViewModel", e)
            throw e
        }
    }
}