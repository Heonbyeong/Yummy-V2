package com.example.yummy_v2.ui.home

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yummy_v2.BuildConfig
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseActivity
import com.example.yummy_v2.databinding.ActivityAddressBinding

class AddressActivity : BaseActivity<ActivityAddressBinding>(R.layout.activity_address) {
    private lateinit var addressViewModel : AddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        binding.addressRv.layoutManager = LinearLayoutManager(this)

        binding.addressEt.setOnEditorActionListener{v, actionId, event ->
            var handled = false
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                addressViewModel.searchAddress(BuildConfig.ADDR_API_KEY,
                    "json", 1, 10, binding.addressEt.getText().toString())
                handled = true
            }
            return@setOnEditorActionListener handled
        }

        addressViewModel.liveData.observe(this, Observer {
            val addressAdapter = AddressRVAdapter(addressViewModel.liveData)
            binding.addressRv.adapter = addressAdapter
        })
    }
}