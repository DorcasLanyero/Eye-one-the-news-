package com.example.eyeOnTheNews.ui.navigation
/**
 * The `NewsNavigationActions` class models the navigation actions in the app.
 * It provides methods to navigate to each screen in the application.
 *
 * @property [navController] The `NavHostController` that controls the navigation between the screens.
 *
 * @method navigateToLogin This method navigates to the login screen.
 * @method navigateToHome This method navigates to the home screen.
 * @method navigateToArticleScreen This method navigates to the article screen. It takes a `newsArticleId` as an argument.
 * @method navigateToCategoryScreen This method navigates to the category screen. It takes a `newsCategory` as an argument.
 */

import android.app.Activity
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eyeOnTheNews.ui.article.ArticleScreen
import com.example.eyeOnTheNews.ui.article.NewsArticleViewModel
import com.example.eyeOnTheNews.ui.authentication.loginScreen
import com.example.eyeOnTheNews.ui.category.CategoryScreen
import com.example.eyeOnTheNews.ui.category.CategoryViewModel
import com.example.eyeOnTheNews.ui.home.HomeScreen
import kotlinx.coroutines.CoroutineScope


@Composable
fun EyeOnTheNewsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = NewsDestinations.HOME_SCREEN,
    navActions: NewsNavigationActions = remember(navController) {
        NewsNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(NewsDestinations.LOGIN_SCREEN) {
            loginScreen(hiltViewModel())
        }

        composable(NewsDestinations.HOME_SCREEN) {
            HomeScreen(hiltViewModel(), toArticle = { newsArticleId -> navActions.navigateToArticleScreen(newsArticleId)}, toCategory = { category -> navActions.navigateToCategoryScreen(category)})
        }

        composable(
            route = "ArticleScreen?newsArticleId={newsArticleId}",
            arguments = listOf(navArgument("newsArticleId") { type = NavType.IntType; nullable = false})
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("newsArticleId") ?: 0
            val viewModel: NewsArticleViewModel = hiltViewModel()
            viewModel.fetchArticle(articleId)
            ArticleScreen(articleId, viewModel, navController)
        }

        composable(
            route = "CategoryScreen?newsCategory={newsCategory}",
            arguments = listOf(navArgument("newsCategory") { type = NavType.StringType; nullable = false})
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("newsCategory") ?: "general"
            val viewModel: CategoryViewModel = hiltViewModel()
            viewModel.fetchCategoryArticles(category)
            CategoryScreen(category,viewModel, navController, toArticle = { newsArticleId -> navActions.navigateToArticleScreen(newsArticleId)})
        }

    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3