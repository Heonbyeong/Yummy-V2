package com.example.yummy_v2.data.remote

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