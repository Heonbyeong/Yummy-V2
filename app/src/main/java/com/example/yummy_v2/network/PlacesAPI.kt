package com.example.yummy_v2.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.yummy_v2.BuildConfig
import com.example.yummy_v2.R
import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.ui.recommend.RecommendViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import dagger.hilt.android.internal.managers.ViewComponentManager
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import kotlin.collections.ArrayList

class PlacesAPI(
    private val mContext: Context,
    private val _lat: Double,
    private val _lng: Double,
    private val _map: GoogleMap,
    private val viewModel: RecommendViewModel
) : Thread() {

    private val lat_list = ArrayList<Double>()
    private val lng_list = ArrayList<Double>()
    private val vicinity_list = ArrayList<String>()
    private val name_list = ArrayList<String>()
    private val marker_list = ArrayList<Marker>()
    private val context : Context =
        if(mContext is ViewComponentManager.FragmentContextWrapper)
            mContext.baseContext
        else mContext

    override fun run() {
        super.run()
        try {
            lat_list.clear()
            lng_list.clear()
            vicinity_list.clear()

            val request =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$_lat,$_lng&radius=5000&language=ko&type=restaurant&key=${BuildConfig.MAPS_API_KEY}"
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

                    val place = Place(name, vicinity, lat, lng)
                    viewModel.insert(place)
                }
                placeMarker()
            } else {
                Toast.makeText(mContext, "가져온 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun placeMarker() {
        for(marker: Marker in marker_list){
            marker.remove()
        }
        marker_list.clear()

        (context as Activity).runOnUiThread{
            for(i in 0 until lat_list.size){
                val options = MarkerOptions()
                val pos = LatLng(lat_list[i], lng_list[i])
                options.apply {
                    position(pos)
                    title(name_list[i])
                    snippet(vicinity_list[i])
                    icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_marker))
                }
                val marker = _map.addMarker(options)
                marker_list.add(marker!!)
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int) : BitmapDescriptor{
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}