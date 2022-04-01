package com.example.yummy_v2.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private const val CONNECT_TIME_OUT: Long = 15
    private const val WRITE_TIME_OUT: Long = 15
    private const val READ_TIME_OUT: Long = 15
    private var retrofit: Retrofit
    private var addressAPI: AddressAPI

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(httpLoggingInterceptor)
        connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
    }.build()

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.juso.go.kr/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        addressAPI = retrofit.create(AddressAPI::class.java)
    }

    fun getAddressAPI() = addressAPI
}