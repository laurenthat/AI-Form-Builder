package com.sbma.linkup.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardDao
import com.sbma.linkup.connection.Connection
import com.sbma.linkup.connection.ConnectionCard
import com.sbma.linkup.connection.ConnectionCardDao
import com.sbma.linkup.connection.ConnectionDao
import com.sbma.linkup.tag.Tag
import com.sbma.linkup.tag.TagDao
import com.sbma.linkup.user.User
import com.sbma.linkup.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Room Database
 */
@Database(
    entities = [User::class, Card::class, Connection::class, Tag::class, ConnectionCard::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    // TODO: should be renamed to cardDao
    abstract fun userCardDao(): CardDao

    // TODO: should be renamed to connectionDao
    abstract fun userConnectionDao(): ConnectionDao
    abstract fun connectionCardDao(): ConnectionCardDao
    abstract fun tagDao(): TagDao


    companion object {
        private var DB_NAME: String = "linkup"

        @Volatile
        private var Instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback())
                    .build()
                    .also { Instance = it }
            }
        }


        /**
         * Database initializer
         */
        private class AppDatabaseCallback : Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        Instance!!.populateDatabase()
                    }
                }
            }
        }
    }

    /**
     * Database initializer
     */
    fun populateDatabase() {
        Timber.d("one time populating the database")
    }

}
