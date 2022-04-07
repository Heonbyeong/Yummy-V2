package com.example.yummy_v2.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Update
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("SELECT * FROM Place")
    fun getAll() : LiveData<List<Place>>
}