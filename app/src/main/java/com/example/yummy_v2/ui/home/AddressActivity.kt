package com.example.yummy_v2.ui.home

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.example.yummy_v2.BuildConfig
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseActivity
import com.example.yummy_v2.databinding.ActivityAddressBinding

class AddressActivity : BaseActivity<ActivityAddressBinding>(R.layout.activity_address) {
    private val viewModel = AddressViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        val addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)

        binding.addressEt.setOnEditorActionListener{v, actionId, event ->
            var handled = false
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                viewModel.searchAddress(BuildConfig.ADDR_API_KEY,
                    "json", 1, 30, binding.addressEt.getText().toString())
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }
}