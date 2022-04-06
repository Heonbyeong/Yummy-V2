package com.example.yummy_v2.ui.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yummy_v2.R
import com.example.yummy_v2.databinding.RestaurantsItemBinding
import com.example.yummy_v2.model.local.Place

class RestaurantsRVAdapter(private val items: List<Place>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: RestaurantsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.restaurants_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            holder.apply {
                onBind(items[position])
            }
        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val binding: RestaurantsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(item : Place) {
            binding.restaurantsTv.text = item.name
            binding.addrTv.text = item.address
        }
    }
}