package com.example.yummy_v2.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressAPI {
    @GET("addrlink/addrLinkApiJsonp.do")
    suspend fun searchAddress(
        @Query("confmKey") confmKey: String,
        @Query("resultType") resultType: String,
        @Query("currentPage") currentPage: String,
        @Query("countPerPage") countPerPage: String,
        @Query("keyword") keyword: String
    ) : Response<String>
}