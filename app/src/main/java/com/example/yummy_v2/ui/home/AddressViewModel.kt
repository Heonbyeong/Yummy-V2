package com.example.yummy_v2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yummy_v2.model.remote.AddressRepositoryImpl
import com.example.yummy_v2.model.remote.AddressResponse
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {
    private val repository = AddressRepositoryImpl()
    private val _liveData: MutableLiveData<AddressResponse> = MutableLiveData()
    val liveData : LiveData<AddressResponse> get() = _liveData

    fun searchAddress(
        confmKey: String,
        resultType: String,
        currentPage: Int,
        countPerPage: Int,
        keyword: String
    ) {
        viewModelScope.launch {
            val response = repository.searchAddress(confmKey, resultType, currentPage, countPerPage, keyword)
            if(response.isSuccessful){
                if(response.code() == 200)
                    _liveData.postValue(response.body())
            }
        }
    }
}