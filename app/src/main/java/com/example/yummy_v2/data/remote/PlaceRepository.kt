package com.example.yummy_v2.data.remote

import androidx.lifecycle.LiveData
import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.data.local.PlaceDao

class PlaceRepository(private val placeDao: PlaceDao) {
    val getAll : LiveData<List<Place>> = placeDao.getAll()

    suspend fun insert(place: Place) {
        placeDao.insert(place)
    }

    suspend fun update(place: Place) {
        placeDao.update(place)
    }

    suspend fun delete(place: Place) {
        placeDao.delete(place)
    }
}