package com.example.yummy_v2.util

import android.app.Activity
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.yummy_v2.R
import com.example.yummy_v2.databinding.AddressBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetDialogUtil(private val activity: Activity) {

    lateinit var binding : AddressBottomBinding

    fun setUpDialog(resId: Int) : BottomSheetDialog {
        val view = activity.layoutInflater.inflate(resId, null)
        binding = AddressBottomBinding.inflate(activity.layoutInflater, view as ViewGroup, false)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(binding.root)
        return dialog
    }
}