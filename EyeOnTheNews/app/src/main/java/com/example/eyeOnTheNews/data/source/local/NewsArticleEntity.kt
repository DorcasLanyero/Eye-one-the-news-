package com.example.eyeOnTheNews.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var author: String? = null,
    val title: String,
    val description: String,
    val url: String,
    val source: String,
    val image: String,
    val category: String,
    val language: String,
    val country: String,
    val published: String,
    var saved: Boolean = false
)