package com.example.yummy_v2.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yummy_v2.R
import com.example.yummy_v2.databinding.AddressItemBinding
import com.example.yummy_v2.data.remote.AddressResponse

class AddressRVAdapter(private val items: AddressResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v: View, position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }

    private lateinit var binding: AddressItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.address_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            holder.apply {
                bind(items.results.juso[position])
            }
        }
    }

    override fun getItemCount() = items.results.juso.size

    inner class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressResponse.ResultResponse.JusoResponse){
            binding.addr1Tv.text = item.roadAddr
            binding.addr2Tv.text = item.jibunAddr

            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnClickListener{
                    itemClickListener?.onItemClick(itemView, position)
                }
            }
        }
    }

}