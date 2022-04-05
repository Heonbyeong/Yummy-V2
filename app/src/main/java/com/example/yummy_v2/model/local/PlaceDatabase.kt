package com.example.yummy_v2.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Place::class], version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao() : PlaceDao

    companion object {
        private var instance: PlaceDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        @Synchronized
        fun getInstance(context: Context): PlaceDatabase? {
            synchronized(PlaceDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaceDatabase::class.java,
                    "place-database"
                ).build()
            }
            return instance
        }
    }
}