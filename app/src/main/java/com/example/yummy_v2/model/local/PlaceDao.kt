package com.example.yummy_v2.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface PlaceDao {
    @Insert
    fun insert(place: Place)

    @Update
    fun update(place: Place)

    @Delete
    fun delete(place: Place)
}