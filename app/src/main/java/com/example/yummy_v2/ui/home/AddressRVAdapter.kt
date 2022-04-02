package com.example.yummy_v2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.yummy_v2.R
import com.example.yummy_v2.databinding.AddressItemBinding
import com.example.yummy_v2.model.remote.AddressResponse

class AddressRVAdapter(private val items: LiveData<AddressResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: AddressItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.address_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            holder.binding.addr1Tv.text = items.value!!.results.juso[position].roadAddr
            holder.binding.addr2Tv.text = items.value!!.results.juso[position].jibunAddr
        }
    }

    override fun getItemCount() = items.value!!.results.juso.size

    inner class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}