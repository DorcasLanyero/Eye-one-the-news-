package com.example.eyeOnTheNews.di

import android.content.Context
import com.example.eyeOnTheNews.data.repository.NewsRepository
import com.example.eyeOnTheNews.data.repository.NewsRepositoryImplementation
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsDao
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsRoomDB
import com.example.eyeOnTheNews.data.source.local.LocalDataSource
import com.example.eyeOnTheNews.data.source.local.LocalDataSourceImpl
import com.example.eyeOnTheNews.data.source.remote.RemoteDataSource
import com.example.eyeOnTheNews.data.source.remote.RemoteDataSourceImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * The `DataModule` consists of several Dagger modules that provide dependencies for the data layer of the application.
 *
 * `RepositoryModule`:  Provides the `NewsRepository` singleton instance. It uses the `LocalDataSource` and `RemoteDataSourceImpl` instances and the `CoroutineDispatcher` for IO operations.
 *
 * `DataSourceModule`: Provides the `Gson`, `Retrofit`, `RemoteDataSource`, and `LocalDataSource` singleton instances. It sets up the `Retrofit` instance with the base URL, `GsonConverterFactory`, and an `OkHttpClient` with an interceptor for adding the Authorization header. It also provides the `RemoteDataSource` and `LocalDataSource` instances using the `Retrofit` and `EyeOnTheNewsDao` instances respectively.
 *
 * `DatabaseModule`: Provides the `EyeOnTheNewsRoomDB` and `EyeOnTheNewsDao` singleton instances. It sets up the `EyeOnTheNewsRoomDB` instance with the application context and provides the `EyeOnTheNewsDao` instance using the `EyeOnTheNewsRoomDB` instance. It also provides a named `EyeOnTheNewsRoomDB` instance for testing purposes.
 */

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideNewsRepository(
        localDataSource: EyeOnTheNewsDao,
        remoteDataSource: RemoteDataSourceImpl,
        ioDispatcher: CoroutineDispatcher
    ): NewsRepository {
        return NewsRepositoryImplementation(localDataSource, remoteDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://api.mediastack.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "33e14851337dbd6646a91ecc6688a27ca")
                        .build()
                    chain.proceed(request)
                }
                .build()
        )
        .build()

    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDataSource =
        retrofit.create(RemoteDataSource::class.java)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        ioDispatcher: CoroutineDispatcher,
        EyeOnTheNewsDao: EyeOnTheNewsDao,

        ): LocalDataSource {
        return LocalDataSourceImpl(EyeOnTheNewsDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideEyeOnTheNewsRoomDataBase(@ApplicationContext context: Context): EyeOnTheNewsRoomDB {
        return EyeOnTheNewsRoomDB.getDatabase(context.applicationContext)
    }

    @Singleton
    @Provides
    fun provideInspectionDao(
        @ApplicationContext context: Context,
        database: EyeOnTheNewsRoomDB
    ): EyeOnTheNewsDao {
        val dao = database.inspectionDao()
        return dao

    }

    @Provides
    @Named("testDatabase")
    fun provideEyeOneTheNewsRoomDataBase(@ApplicationContext context: Context): EyeOnTheNewsRoomDB {
        return EyeOnTheNewsRoomDB.getDatabase(context)
    }
}
