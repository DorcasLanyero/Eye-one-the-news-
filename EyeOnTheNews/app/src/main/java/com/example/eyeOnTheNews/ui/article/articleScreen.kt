package com.example.eyeOnTheNews.ui.article

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.size.Precision
import coil.size.Scale
import com.example.autotranstandaloneinspection.R
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.ui.home.Category
import com.example.eyeOnTheNews.ui.home.contrastAgainst
import com.example.eyeOnTheNews.ui.home.reformatDate
import kotlinx.coroutines.launch


@Composable
fun ArticleScreen(
    articleId: Int,
    viewModel: NewsArticleViewModel,
    navController: NavHostController
) {
    val newsArticle by viewModel.newsArticle.observeAsState()

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (newsArticle != null) {
                    ArticleContent(newsArticle!!, navController)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                        CircularProgressIndicator()
                    }
                }
            }
        },
    )
}

@Composable
fun ArticleContent(newsArticle: NewsArticle, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = rememberImagePainter(
                        data = newsArticle.image,
                        builder = {
                            scale(Scale.FILL)
                            precision(Precision.EXACT)
                        }
                    ),
                    contentDescription = "News article image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                TopBar(navController)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = 60.dp, horizontal = 16.dp)
                ) {
                    Category(newsArticle.category)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = newsArticle.title, style = MaterialTheme.typography.titleLarge,
                        color = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = reformatDate(newsArticle.published), style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground))
                }
            }
            Surface(
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp).offset(y = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = newsArticle.source, style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = newsArticle.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(55.dp))
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsArticle.url))
                                ContextCompat.startActivity(context, intent, null)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Read Full Article")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    Surface(color = Color.Transparent, elevation = 0.dp) {
        TopAppBar(
            colors = TopAppBarColors(containerColor = Color.Transparent, actionIconContentColor = Color.White, navigationIconContentColor = Color.White, scrolledContainerColor = Color.Transparent, titleContentColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack()}) {
                    Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "Back Icon")
                }
                Spacer(modifier = Modifier.width(16.dp))
            },
            title = {},
            actions = {

                Row {
                    IconButton(onClick = { /* Handle Search click */ }) {
                        Icon(painter = painterResource(id = R.drawable.bookmark_black_24), contentDescription = "Save Icon")
                    }
                    IconButton(onClick = { /* Handle Notifications click */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Notifications")
                    }
                }
            }
        )
    }
}

