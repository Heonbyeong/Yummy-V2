package com.example.yummy_v2.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseFragment
import com.example.yummy_v2.databinding.FragmentHomeBinding
import com.example.yummy_v2.network.PlacesAPI
import com.example.yummy_v2.ui.recommend.RecommendFragment
import com.example.yummy_v2.ui.recommend.RecommendViewModel
import com.example.yummy_v2.util.PrefsHelper
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private val gps =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                if(checkLocationServicesStatus()){
                    needRequest = true
                }
            }
        }
    private var needRequest = false

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView

    private var currentMarker: Marker? = null
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var location : Location
    private val UPDATE_INTERVAL_MS = 100L
    private val FASTEST_UPDATE_INTERVAL_MS = 50L

    private lateinit var currentPosition : LatLng

    private val recommendViewModel : RecommendViewModel by viewModels()
    private var isRun = false

    @Inject
    lateinit var prefs : PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL_MS
            fastestInterval = FASTEST_UPDATE_INTERVAL_MS
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        recommendViewModel
        prefs

        mapView = binding.googleMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        isRun = false
        val intent = requireActivity().intent

        if(checkPermission()){
            if(!intent.hasExtra("roadAddr")){
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()

        if(mFusedLocationClient != null){
            mFusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        if(!checkPermission()){
            setDefaultLocation()
        }
        permissionSnackBar()
    }

    private fun permissionSnackBar() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates()
        } else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])){
                Snackbar.make(binding.layoutHome, R.string.snack_bar_message,
                    Snackbar.LENGTH_INDEFINITE).setAction("확인") {
                    ActivityCompat.requestPermissions(
                        requireActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE
                    )
                }.show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun checkPermission() : Boolean {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun startLocationUpdates() {
        if(!checkLocationServicesStatus()){
            showDialogForLocationSetting(gps)
        } else {
            val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
            val intent = requireActivity().intent
            val roadAddr = intent.getStringExtra("roadAddr")

            if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return
            }
            if(intent.hasExtra("roadAddr")){
                setSearchLocation(getLocationFromAddress(), roadAddr!!)
            } else {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
            }
            if(checkPermission()){
                mMap.isMyLocationEnabled = true
            }
        }
    }

    private fun showDialogForLocationSetting(startForResult : ActivityResultLauncher<Intent>) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setTitle(getString(R.string.dialog_title))
            setMessage(getString(R.string.dialog_message))
            setCancelable(true)
            setPositiveButton("설정"
            ) { dialog, id ->
                val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startForResult.launch(callGPSSettingIntent)
            }
        }
    }

    private fun setDefaultLocation() {
        if(currentMarker != null) currentMarker!!.remove()

        val DEFAULT_LOCATION = LatLng(37.56, 126.97)

        val markerOptions = MarkerOptions()
        markerOptions.apply {
            position(DEFAULT_LOCATION)
            title(getString(R.string.default_marker_title))
            snippet(getString(R.string.default_marker_snippet))
            draggable(true)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            currentMarker = mMap.addMarker(markerOptions)
        }

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15F)
        mMap.moveCamera(cameraUpdate)
    }

    private fun setCurrentLocation(location: Location) {
        if(currentMarker != null) currentMarker!!.remove()

        val currentLatLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap.moveCamera(cameraUpdate)
    }

    private fun checkLocationServicesStatus() : Boolean {
        val locationManager : LocationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val locationList = locationResult.locations
            if(locationList.size > 0) {
                location = locationList[locationList.size - 1]

                currentPosition = LatLng(location.latitude, location.longitude)

                if(!isRun){
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, 15F)
                    mMap.moveCamera(cameraUpdate)
                    getLocationFromLocation()
                    PlacesAPI(requireContext(), location.latitude, location.longitude, mMap, recommendViewModel).start()

                    prefs.curLat = currentPosition.latitude.toString()
                    prefs.curLng = currentPosition.longitude.toString()
                    isRun = true
                }

            }
        }
    }

    fun addressActivity() {
        val intent = Intent(requireContext(), AddressActivity::class.java)
        requireContext().startActivity(intent)
    }

    private fun setSearchLocation(latLng: LatLng, addr: String) {
        mMap.clear()

        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title(requireActivity().getString(R.string.search_marker_title))
            snippet(addr)
        }
        mMap.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15F)
        mMap.moveCamera(cameraUpdate)
        binding.addressTv.text = addr
        PlacesAPI(requireContext(), latLng.latitude, latLng.longitude, mMap, recommendViewModel).start()
    }

    private fun getLocationFromLocation() {
        val geocoder = Geocoder(requireContext())
        val address = geocoder.getFromLocation(currentPosition.latitude, currentPosition.longitude, 2)
        binding.addressTv.text = address[0].getAddressLine(0).removePrefix("대한민국")
    }

    private fun getLocationFromAddress() : LatLng {
        val geocoder = Geocoder(requireContext())
        val intent = requireActivity().intent
        val address = geocoder.getFromLocationName(intent.getStringExtra("roadAddr"), 1)
        val latLng = LatLng(address[0].latitude, address[0].longitude)
        return latLng
    }
}