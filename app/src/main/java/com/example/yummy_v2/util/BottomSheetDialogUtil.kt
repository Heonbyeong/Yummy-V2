package com.example.yummy_v2.util

import android.app.Activity
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.yummy_v2.R
import com.example.yummy_v2.databinding.AddressBottomBinding
import com.example.yummy_v2.databinding.RestaurantsBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetDialogUtil(private val activity: Activity) {

    lateinit var binding : AddressBottomBinding
    lateinit var binding2 : RestaurantsBottomBinding

    fun setUpDialog(resId: Int) : BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        when(resId) {
            R.layout.address_bottom -> {
                val view = activity.layoutInflater.inflate(resId, null)
                binding = AddressBottomBinding.inflate(activity.layoutInflater, view as ViewGroup, false)
                dialog.setContentView(binding.root)
            }
            R.layout.restaurants_bottom -> {
                val view = activity.layoutInflater.inflate(resId, null)
                binding2 = RestaurantsBottomBinding.inflate(activity.layoutInflater, view as ViewGroup, false)
                dialog.setContentView(binding2.root)
            }
        }
        return dialog
    }
}