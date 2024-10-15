package com.example.eyeOnTheNews.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.eyeOnTheNews.ui.navigation.NewsDestinationsArgs.NEWS_ARTICLE_ID
import com.example.eyeOnTheNews.ui.navigation.NewsDestinationsArgs.NEWS_CATEGORY

/**
 * Screens used in [NewsDestinations]
 */
private object NewsScreens {
    const val LOGIN_SCREEN = "loginScreen"
    const val HOME_SCREEN = "HomeScreen"
    const val NEWS_ARTICLE_SCREEN = "ArticleScreen"
    const val NEWS_CATEGORY_SCREEN = "CategoryScreen"

}

/**
 * Arguments used in [NewsDestinations] routes
 */
object NewsDestinationsArgs {
    const val NEWS_ARTICLE_ID = "newsArticleId"
    const val NEWS_CATEGORY = "newsCategory"
}

/**
 * Destinations used in the [MainActivity]
 */
object NewsDestinations {
    const val LOGIN_SCREEN = NewsScreens.LOGIN_SCREEN
    const val HOME_SCREEN = NewsScreens.HOME_SCREEN
    const val NEWS_ARTICLE_ROUTE = NewsScreens.NEWS_ARTICLE_SCREEN
    const val NEWS_CATEGORY_ROUTE = NewsScreens.NEWS_CATEGORY_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
class NewsNavigationActions(private val navController: NavHostController) {
    fun navigateToLogin() {
        navController.navigate(NewsDestinations.LOGIN_SCREEN) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true

        }
    }

    fun navigateToHome() {
        navController.navigate(NewsDestinations.HOME_SCREEN) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true

        }
    }

    fun navigateToArticleScreen(newsArticleId: Int) {
        navController.navigate(
            NewsDestinations.NEWS_ARTICLE_ROUTE.let {
                "$it?$NEWS_ARTICLE_ID=$newsArticleId"
            }
        )
    }

    fun navigateToCategoryScreen(newsCategory: String) {
        navController.navigate(
            NewsDestinations.NEWS_CATEGORY_ROUTE.let {
                "$it?$NEWS_CATEGORY=$newsCategory"
            }
        )
    }
}