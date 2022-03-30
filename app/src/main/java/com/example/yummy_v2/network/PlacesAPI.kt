package com.example.yummy_v2.network

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class PlacesAPI(
    private val mContext: Context,
    private val _lat: Double,
    private val _lng: Double,
    private val _map: GoogleMap
) : Thread() {

    private val lat_list = ArrayList<Double>()
    private val lng_list = ArrayList<Double>()
    private val vicinity_list = ArrayList<String>()
    private val name_list = ArrayList<String>()
    private val marker_list = ArrayList<Marker>()

    override fun run() {
        super.run()
        try {
            lat_list.clear()
            lng_list.clear()
            vicinity_list.clear()

            val request =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$_lat,$_lng&radius=$1000&language=ko&type=restaurant&key=${
                    Properties().getProperty("MAPS_API_KEY")
                }"
            val url = URL(request)
            val conn = url.openConnection()

            val inputStream = conn.getInputStream()
            val inputStreamReader = InputStreamReader(inputStream, "utf-8")
            val bufferReader = BufferedReader(inputStreamReader)
            var str: String? = null
            val strBuffer = StringBuffer()

            do {
                str = bufferReader.readLine()
                if (str != null) {
                    strBuffer.append(str)
                }
            } while (str != null)
            val rec_data = strBuffer.toString()
            val root = JSONObject(rec_data)
            val status = root.getString("status")

            if (status.equals("OK")) {
                val result = root.getJSONArray("results")
                for (i in 0 until result.length()) {
                    val obj = result.getJSONObject(i)
                    val geometry = obj.getJSONObject("geometry")
                    val location = geometry.getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lng = location.getDouble("lng")

                    val name = obj.getString("name")
                    val vicinity = obj.getString("vicinity")

                    lat_list.add(lat)
                    lng_list.add(lng)
                    name_list.add(name)
                    vicinity_list.add(vicinity)
                }
                placeMarker()
            } else {
                Toast.makeText(mContext, "가져온 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun placeMarker() {
        for(marker: Marker in marker_list){
            marker.remove()
        }
        marker_list.clear()

        for(i in 0 until lat_list.size){
            val options = MarkerOptions()
            val pos = LatLng(lat_list[i], lng_list[i])
            options.position(pos)
            options.title(name_list[i])
            options.snippet(vicinity_list[i])

            val marker = _map.addMarker(options)
            marker_list.add(marker!!)
        }
    }
}