package com.example.yummy_v2.data.remote

import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.data.local.PlaceDao

class PlaceRepository(private val placeDao: PlaceDao) {

    suspend fun insert(place: Place) {
        placeDao.insert(place)
    }

    suspend fun update(place: Place) {
        placeDao.update(place)
    }

    suspend fun delete(place: Place) {
        placeDao.delete(place)
    }

    suspend fun getAll() = placeDao.getAll()
}