package com.example.yummy_v2.model.remote

import retrofit2.Response

interface AddressRepository {
    suspend fun searchAddress(
        confmKey: String,
        resultType: String,
        currentPage: Int,
        countPerPage: Int,
        keyword: String
    ) : Response<AddressResponse>
}