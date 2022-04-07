package com.example.yummy_v2.di

import com.example.yummy_v2.data.local.PlaceDao
import com.example.yummy_v2.data.remote.AddressRepository
import com.example.yummy_v2.data.remote.PlaceRepository
import com.example.yummy_v2.network.AddressAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAddressRepository(api: AddressAPI) : AddressRepository =
        AddressRepository(api)

    @Singleton
    @Provides
    fun providePlaceRepository(dao: PlaceDao) : PlaceRepository =
        PlaceRepository(dao)
}