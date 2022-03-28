package com.example.yummy_v2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseFragment
import com.example.yummy_v2.databinding.FragmentHomeBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val mapFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.google_map) as SupportMapFragment
            mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}