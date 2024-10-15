package com.example.eyeOnTheNews.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.size.Precision
import coil.size.Scale
import com.example.autotranstandaloneinspection.R
import com.example.eyeOnTheNews.data.repository.NewsArticle
import com.example.eyeOnTheNews.ui.theme.EyeOnTheNewsTheme
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), toArticle: (Int) -> Unit, toCategory: (String) -> Unit) {
    val uiState = viewModel.uiState.collectAsState()

    val carouselArticles = if (uiState.value.carouselArticles.value.isNotEmpty()) {
        uiState.value.carouselArticles.value
    } else {
        newsArticles
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon click */ }) {
                        Icon(
                            painterResource(id = R.drawable.ic_closer),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                title = { Spacer(modifier = Modifier.fillMaxWidth()) },
                actions = {
                    IconButton(onClick = { /* Handle Search click */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Handle Notifications click */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {

                if (uiState.value.isLoading.value) {
                    // Show loading indicator
                    CircularProgressIndicator()
                } else {
                    HomeContent(uiState.value, carouselArticles, toArticle, toCategory)
                }
            }
        },
        bottomBar = { BottomBar() },
    )
}

@Composable
fun emphasis(category: String) {
    Surface(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .clickable { /* Handle Home click */ }
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                category,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                fontSize = 20.sp
            )
            Spacer(Modifier.weight(1f, true))
            Text(
                "View all",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { /* Handle Home click */ }
                .height(45.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Home")
                Text(
                    "Home",
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
        }
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { /* Handle Save click */ }) {
            Icon(painter = painterResource(id = R.drawable.bookmark_black_24), contentDescription = "Your Icon")
        }
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { /* Handle Profile click */ }) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
        }
    }
}

@Composable
fun HomeContent(
    state: HomeViewState,
    carouselArticles: List<NewsArticle>,
    toArticle: (Int) -> Unit,
    toCategory: (String) -> Unit
) {
    Column {
        Row {
            emphasis("Breaking News")
        }
        Row { NewsCarousel(carouselArticles, toCategory) }

        Row {
            emphasis("Recommended")
        }
        LazyColumn {
            items(state.newsArticles.value.size) { index ->
                NewsCard(state.newsArticles.value[index],toArticle)
            }
        }
    }
}
@Composable
fun NewsCard(newsArticle: NewsArticle, toArticle: (Int) -> Unit) {
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

            Column( horizontalAlignment = Alignment.Start,

                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .heightIn(min = 100.dp)
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

val newsArticles = listOf<NewsArticle>(
    NewsArticle(
        1,
        "Dorcas Lanyero",
        "This is a news article title Sample for testing",
        "This is a news article description Sample for testing",
        "https://unsplash.com/photos/people-walking-on-street-during-daytime-S0Ig4N1bUT8",
        "BBC",
        "https://www.google.com",
        "general",
        "English",
        "Uganda",
        "${getRandomDate()}",
        false
    ),
    NewsArticle(
        2,
        "Dorcas Lanyero",
        "This is a news article title Sample for testing",
        "This is a news article description Sample for testing",
        "https://unsplash.com/photos/people-walking-on-street-during-daytime-S0Ig4N1bUT8",
        "BBC",
        "https://www.google.com",
        "sports",
        "English",
        "Uganda",
        "${getRandomDate()}",
        false
    ),
    NewsArticle(
        3,
        "Dorcas Lanyero",
        "This is a news article title Sample for testing",
        "This is a news article description Sample for testing",
        "https://unsplash.com/photos/people-walking-on-street-during-daytime-S0Ig4N1bUT8",
        "BBC",
        "https://www.google.com",
        "education",
        "English",
        "Uganda",
        "${getRandomDate()}",
        false
    ),
    NewsArticle(
        4,
        "Dorcas Lanyero",
        "This is a news article title Sample for testing",
        "This is a news article description Sample for testing",
        "https://unsplash.com/photos/people-walking-on-street-during-daytime-S0Ig4N1bUT8",
        "BBC",
        "https://www.google.com",
        "politics",
        "English",
        "Uganda",
        "${getRandomDate()}",
        false
    )
)

@Composable
fun NewsCarousel(newsArticles: List<NewsArticle>, toCategory: (String) -> Unit){
    val pagerState = rememberPagerState(0, 0.2F) { newsArticles.size }
    val coroutineScope = rememberCoroutineScope()
    Column {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSpacing = 10.dp
        ) { page ->
            Column {
                NewsCarouselCard(
                    newsArticles[page], toCategory,
                    if (page == pagerState.currentPage) 200.dp else 200.dp
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    HorizontalPagerIndicator(
                        pageCount = newsArticles.size,
                        pagerState = pagerState,
                        activeColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                val currentPage = pagerState.currentPage
                                val totalPages = newsArticles.size
                                val nextPage =
                                    if (currentPage < totalPages - 1) currentPage + 1 else 0
                                coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                            }

                    )
                }
            }
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .collect { currentPage ->
                        pagerState.animateScrollToPage(currentPage)
                    }
            }
        }
    }
}

@Composable
fun NewsCarouselCard(newsArticle: NewsArticle, toCategory: (String) -> Unit, height: Dp) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(height)
            .clickable { toCategory(newsArticle.category) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box {
            Image(
                painter = rememberImagePainter(
                    data = newsArticle.image,
                    builder = {
                        scale(Scale.FILL)
                        precision(Precision.EXACT)
                    }),
                //painter = painterResource(id = R.drawable.carousel_4),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            {
                Row(modifier = Modifier.weight(2f)) {
                    Category(category = newsArticle.category)
                }
                Row(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(
                            text = newsArticle.author ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            color = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Column {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "News Source",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                    Column {
                        Text(
                            text = reformatDate(newsArticle.published),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            color = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Row(modifier = Modifier.weight(2f)) {
                    Text(
                        newsArticle.title, style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 8.dp),
                        color = Color.Black.contrastAgainst(MaterialTheme.colorScheme.onBackground),
                    )

                }
            }
        }
    }
}

@Composable
fun Category(category: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .height(45.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                category,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
    }

}

fun truncateTitle(title: String): String {
    val words = title.split(" ")
    return if (words.size > 8) {
        words.take(10).joinToString(" ")+ "..."
    } else {
        title
    }
}

fun Color.contrastAgainst(background: Color): Color {
    val foregroundLuminance = luminance()
    val backgroundLuminance = background.luminance()

    val contrast = (foregroundLuminance + 0.05) / (backgroundLuminance + 0.05)

    return if (contrast < 1.8) Color.White else Color.Black
}

fun reformatDate(date: String): String {
  return date.substringBefore('T')
}

fun getRandomDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

    val startMillis = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000)
    val endMillis = System.currentTimeMillis()

    val randomMillis = (startMillis..endMillis).random()
    val randomDate = Date(randomMillis)

    return formatter.format(randomDate)
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun homeScreen() {
    EyeOnTheNewsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                //HomeScreen()
            }
        }
    }
}