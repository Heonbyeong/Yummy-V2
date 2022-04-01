package com.example.yummy_v2.model.remote

import retrofit2.Response

interface AddressRepository {
    suspend fun searchAddress(
        confmKey: String,
        resultType: String,
        currentPage: String,
        countPerPage: String,
        keyword: String
    ) : Response<AddressResponse>
}