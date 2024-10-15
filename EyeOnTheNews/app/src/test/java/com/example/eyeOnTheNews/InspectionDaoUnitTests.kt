package com.example.eyeOnTheNews

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsDao
import com.example.eyeOnTheNews.data.source.local.EyeOnTheNewsRoomDB
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EyeOnTheNewsDaoTest {

    private lateinit var db: EyeOnTheNewsRoomDB
    private lateinit var eyeOnTheNewsDao: EyeOnTheNewsDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EyeOnTheNewsRoomDB::class.java).build()
        eyeOnTheNewsDao = db.inspectionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
}