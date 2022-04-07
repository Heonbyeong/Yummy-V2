package com.example.yummy_v2.data.remote

import com.example.yummy_v2.network.AddressAPI
import com.example.yummy_v2.network.SafeApiCall
import retrofit2.Response
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val retrofit : AddressAPI
) :SafeApiCall() {
    suspend fun searchAddress(
        confmKey: String,
        resultType: String,
        currentPage: Int,
        countPerPage: Int,
        keyword: String
    ): Response<AddressResponse> {
        return safeApiCall {
            retrofit.searchAddress(confmKey, resultType, currentPage, countPerPage, keyword)
        }
    }

}