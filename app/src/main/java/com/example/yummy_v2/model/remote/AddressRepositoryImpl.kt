package com.example.yummy_v2.model.remote

import com.example.yummy_v2.network.RetrofitBuilder
import com.example.yummy_v2.network.SafeApiCall
import retrofit2.Response

class AddressRepositoryImpl : AddressRepository, SafeApiCall() {
    override suspend fun searchAddress(
        confmKey: String,
        resultType: String,
        currentPage: Int,
        countPerPage: Int,
        keyword: String
    ): Response<AddressResponse> {
        return safeApiCall {
            RetrofitBuilder.getAddressAPI()
                .searchAddress(confmKey, resultType, currentPage, countPerPage, keyword)
        }
    }

}