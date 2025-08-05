package com.youngsik.jinada.data.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.youngsik.jinada.data.repository.NaverApiRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NaverApiClient {
    private val BASE_URL = "https://maps.apigw.ntruss.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val naverApi: NaverApiRepository by lazy {
        retrofit.create(NaverApiRepository::class.java)
    }
}