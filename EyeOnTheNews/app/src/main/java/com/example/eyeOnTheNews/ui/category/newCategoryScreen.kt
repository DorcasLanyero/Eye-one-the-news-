package com.example.eyeOnTheNews.ui.category

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.Precision
import coil.size.Scale
import com.example.autotranstandaloneinspection.R
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.ui.home.reformatDate
import com.example.eyeOnTheNews.ui.home.truncateTitle


@Composable
fun CategoryScreen(category:String, viewModel: CategoryViewModel,navController: NavController,toArticle: (Int) -> Unit) {

    val newsCategoryArticles by viewModel.newsArticlesByCategory.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopBar(navController, category) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (newsCategoryArticles != null) {
                    CategoryContent(category,newsCategoryArticles!!, toArticle)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, category: String) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "back Icon")
            }
        },
        title = { emphasis(category)},
        actions = {Spacer(modifier = Modifier.fillMaxWidth())}
    )
}

@Composable
fun CategoryContent(category: String,
    newsCategoryArticles: List<NewsArticle>,
    toArticle: (Int) -> Unit
) {
    Box {
        Row {

        }
        Row {
            LazyColumn {
                items(newsCategoryArticles.size) { index ->
                    newsCategoryArticles[index].let { newsArticle ->
                        CategoryNewsCard(newsArticle, toArticle)
                    }
                }
        }
    }
} }

@Composable
fun emphasis(category: String) {
Surface(
shape = RoundedCornerShape(50),
modifier = Modifier
.height(50.dp)
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            category.uppercase(),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            fontSize = 20.sp)
    }
    }
}


    // Composable for NewsCard
    @Composable
    fun CategoryNewsCard(newsArticle: NewsArticle, toArticle: (Int) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                    Log.d("News Article selected", "News Article: ${newsArticle.id}")
                    toArticle(newsArticle.id)
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .height(120.dp)
                        .aspectRatio(1f)
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
                        modifier = Modifier
                            .fillMaxSize()
                            .height(IntrinsicSize.Max)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .heightIn(min = 100.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = newsArticle.source,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = truncateTitle(newsArticle.title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 5.dp)
                    )
                    Text(
                        text = "${reformatDate(newsArticle.published)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
