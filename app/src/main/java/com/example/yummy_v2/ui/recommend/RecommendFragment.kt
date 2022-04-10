package com.example.yummy_v2.ui.recommend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseFragment
import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.databinding.FragmentRecommendBinding
import com.example.yummy_v2.util.BottomSheetDialogUtil
import com.example.yummy_v2.util.PrefsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecommendFragment : BaseFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    private lateinit var bottomSheet : BottomSheetDialogUtil
    private val recommendViewModel : RecommendViewModel by viewModels()
    private var restaurantsList : List<Place>? = null

    @Inject
    lateinit var prefs : PrefsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        prefs
        bottomSheet = BottomSheetDialogUtil(requireActivity())
        recommendViewModel.getAll()

        recommendViewModel.restaurants.observe(viewLifecycleOwner) {
            restaurantsList = it
        }
    }

    fun recommend() {
        if(restaurantsList != null){
            binding.blur.visibility = View.VISIBLE
            binding.lottieView.visibility = View.VISIBLE
            val range = (0 until restaurantsList!!.size).random()
            val restaurants = restaurantsList!![range]

            requireActivity().runOnUiThread{
                Handler(Looper.myLooper()!!).postDelayed({
                    binding.blur.visibility = View.INVISIBLE
                    binding.lottieView.visibility = View.INVISIBLE
                    val dialog = bottomSheet.setUpDialog(R.layout.restaurants_bottom)
                    bottomSheet.binding2.nameTv.text = restaurants.name
                    bottomSheet.binding2.bottomAddrTv.text = restaurants.address
                    bottomSheet.binding2.confirmBtn.setOnClickListener{
                        googleMapIntent(
                            prefs.curLat!!.toDouble(),
                            prefs.curLng!!.toDouble(),
                            restaurants.lat,
                            restaurants.lng)
                    }
                    dialog.show()
                }, 2000L)
            }
        }
    }

    private fun googleMapIntent(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double) {
        val uri = "http://maps.google.com/maps?saddr=$startLat,$startLng&daddr=$endLat,$endLng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
            `package` = "com.google.android.apps.maps"
        }
        startActivity(intent)
    }
}