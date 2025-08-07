package com.youngsik.jinada.data.datasource.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.youngsik.jinada.data.repository.NaverApiRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NaverApiClient {
    private val NAVER_MAP_BASE_URL = "https://maps.apigw.ntruss.com/"
    private val NAVER_SEARCH_BASE_URL = "https://openapi.naver.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofitByMap: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NAVER_MAP_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val retrofitBySearch: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NAVER_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val naverMapApi: NaverApiRepository by lazy {
        retrofitByMap.create(NaverApiRepository::class.java)
    }

    val naverSearchApi: NaverApiRepository by lazy {
        retrofitBySearch.create(NaverApiRepository::class.java)
    }
}