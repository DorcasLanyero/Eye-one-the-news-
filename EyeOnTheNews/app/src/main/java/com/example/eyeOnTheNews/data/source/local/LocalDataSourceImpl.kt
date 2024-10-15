package com.example.eyeOnTheNews.data.source.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val newsDao: EyeOnTheNewsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : LocalDataSource