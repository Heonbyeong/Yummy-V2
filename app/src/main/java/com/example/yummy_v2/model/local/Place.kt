package com.example.yummy_v2.model.local

import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

data class Place(
    var name: String,
    var address: String,
    var latLng: LatLng
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}