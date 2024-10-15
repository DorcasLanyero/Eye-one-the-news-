/**
 * The `EyeOnTheNewsRoomDB` abstract class is a Room database for storing `NewsArticleEntity` objects.
 * It provides a singleton instance of the database and a method to access the `EyeOnTheNewsDao` interface.
 *
 * @property INSTANCE The singleton instance of the database.
 *
 * @method getDatabase This method returns the singleton instance of the database. If the instance is null, it creates a new one.
 * It uses the Room database builder to create the database with the application context and the database class.
 * It also adds a callback for prepopulating the database and migrations for updating the database schema.
 *
 * @method inspectionDao This abstract method provides access to the `EyeOnTheNewsDao` interface.
 *
 * The `PrepopulateRoomCallback` inner class is a callback for prepopulating the database.
 * It overrides the `onCreate` method to prepopulate the database when it is created.
 *
 * The `MIGRATION_1_2`, `MIGRATION_2_3`, `MIGRATION_1_3`, and `MIGRATION_3_4` objects are migrations for updating the database schema.
 */

package com.example.eyeOnTheNews.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [NewsArticleEntity::class], version = 4)
@TypeConverters(Converters::class)
abstract class EyeOnTheNewsRoomDB : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: EyeOnTheNewsRoomDB? = null

        fun getDatabase(context: Context): EyeOnTheNewsRoomDB {
            // if the INSTANCE is not null, then return it,
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EyeOnTheNewsRoomDB::class.java,
                    "autotran_database"
                ).addCallback(PrepopulateRoomCallback(context))
                    .addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun inspectionDao(): EyeOnTheNewsDao

    class PrepopulateRoomCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
            }
        }


    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE news_articles ALTER COLUMN author TEXT;")
    }
}
val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE new_news_articles (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                author TEXT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                url TEXT NOT NULL,
                source TEXT NOT NULL,
                image TEXT NOT NULL,
                category TEXT NOT NULL,
                language TEXT NOT NULL,
                country TEXT NOT NULL,
                published TEXT NOT NULL,
                saved INTEGER NOT NULL
            )
        """.trimIndent()
        )
        database.execSQL(
            """
            INSERT INTO new_news_articles (id, author, title, description, url, source, image, category, language, country, published, saved)
            SELECT id, author, title, description, url, source, image, category, language, country, published, saved FROM news_articles
        """.trimIndent()
        )
        database.execSQL("DROP TABLE news_articles")
        database.execSQL("ALTER TABLE new_news_articles RENAME TO news_articles")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS area_code_table")
        database.execSQL("DROP TABLE IF EXISTS damage_code_table")
        database.execSQL("DROP TABLE IF EXISTS damage_table")
        database.execSQL("DROP TABLE IF EXISTS image_table")
        database.execSQL("DROP TABLE IF EXISTS inspection_table")
        database.execSQL("DROP TABLE IF EXISTS severity_code_table")
        database.execSQL("DROP TABLE IF EXISTS type_code_table")
    }
}
