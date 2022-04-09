package com.example.yummy_v2.di

import android.content.Context
import androidx.room.Room
import com.example.yummy_v2.data.local.PlaceDao
import com.example.yummy_v2.data.local.PlaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlaceDatabase
            = Room.databaseBuilder(context, PlaceDatabase::class.java,
        "place-database").build()

    @Singleton
    @Provides
    fun providePlaceDao(db: PlaceDatabase): PlaceDao
        = db.placeDao()
}