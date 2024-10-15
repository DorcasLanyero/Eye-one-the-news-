
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eyeOnTheNews.data.source.remote.RemoteDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class RemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        remoteDataSource = retrofit.create(RemoteDataSource::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testAPICall() = runBlocking {
        val mockResponse = """
        {
        "pagination": {
            "limit": 100,
            "offset": 0,
            "count": 100,
            "total": 293
        },
        "data": [
            {
                "author": "Test Author",
                "title": "Test Title",
                "description": "Test Description",
                "url": "Test URL",
                "source": "Test Source",
                "image": "Test Image",
                "category": "Test Category",
                "language": "Test Language",
                "country": "Test Country",
                "published_at": "Test Published At"
            }
        ]
    }
    """
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val response = remoteDataSource.getAllNews()

        val newsResponse = response.body()
        if (newsResponse != null && newsResponse.data.isNotEmpty()) {
            val firstArticle = newsResponse.data[0]
            assertEquals("Test Author", firstArticle.author)
        }
    }
}