package com.example.yummy_v2.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yummy_v2.BuildConfig
import com.example.yummy_v2.MainActivity
import com.example.yummy_v2.R
import com.example.yummy_v2.base.BaseActivity
import com.example.yummy_v2.databinding.ActivityAddressBinding
import com.example.yummy_v2.util.BottomSheetDialogUtil

class AddressActivity : BaseActivity<ActivityAddressBinding>(R.layout.activity_address) {
    private lateinit var addressViewModel : AddressViewModel
    private val bottomSheet = BottomSheetDialogUtil(this@AddressActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        binding.addressRv.layoutManager = LinearLayoutManager(this)

        binding.addressEt.setOnEditorActionListener{v, actionId, event ->
            var handled = false
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                addressViewModel.searchAddress(BuildConfig.ADDR_API_KEY,
                    "json", 1, 10, binding.addressEt.getText().toString())
                imm.hideSoftInputFromWindow(binding.addressEt.windowToken, 0)
                handled = true
            }
            return@setOnEditorActionListener handled
        }

        addressViewModel.liveData.observe(this, Observer { liveData ->
            val addressAdapter = AddressRVAdapter(liveData)
            binding.listIsEmptyTv.visibility = View.INVISIBLE
            binding.addressRv.adapter = addressAdapter

            addressAdapter.setOnItemClickListener(object: AddressRVAdapter.OnItemClickListener{
                override fun onItemClick(v: View, position: Int) {
                    val dialog = bottomSheet.setUpDialog(R.layout.address_bottom)
                    dialog.show()
                    bottomSheet.binding.bottomAddr1Tv.text = liveData.results.juso[position].roadAddr
                    bottomSheet.binding.bottomAddr2Tv.text = liveData.results.juso[position].jibunAddr
                    bottomSheet.binding.confirmBtn.setOnClickListener{
                        val intent = Intent(this@AddressActivity, MainActivity::class.java).apply {
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra("roadAddr", liveData.results.juso[position].roadAddr)
                        }
                        startActivity(intent)
                    }
                }
            })
        })
    }
}