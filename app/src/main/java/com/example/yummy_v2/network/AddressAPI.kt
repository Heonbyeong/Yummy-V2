package com.example.yummy_v2.network

import com.example.yummy_v2.data.remote.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressAPI {
    @GET("addrlink/addrLinkApi.do")
    suspend fun searchAddress(
        @Query("confmKey") confmKey: String,
        @Query("resultType") resultType: String,
        @Query("currentPage") currentPage: Int,
        @Query("countPerPage") countPerPage: Int,
        @Query("keyword") keyword: String
    ) : Response<AddressResponse>
}