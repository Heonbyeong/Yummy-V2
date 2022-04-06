package com.example.yummy_v2.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_table")
data class Place(
    var name: String,
    var address: String,
    var lat: Double,
    var lng: Double
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}