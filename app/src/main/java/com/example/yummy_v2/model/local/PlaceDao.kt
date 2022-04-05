package com.example.yummy_v2.model.local

import androidx.room.*

@Dao
interface PlaceDao {
    @Insert
    fun insert(place: Place)

    @Update
    fun update(place: Place)

    @Delete
    fun delete(place: Place)

    @Query("SELECT * FROM Place")
    fun getAll() : List<Place>
}