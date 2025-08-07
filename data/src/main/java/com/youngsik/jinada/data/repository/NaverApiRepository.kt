package com.youngsik.jinada.data.repository

import com.youngsik.jinada.data.dataclass.LocalSearchResponseDto
import com.youngsik.jinada.data.dataclass.ReverseGeocodeResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverApiRepository {
    @GET("map-reversegeocode/v2/gc")
    suspend fun getAddressFromCoordinates(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("coords") coords: String,
        @Query("orders") orders: String = "addr,roadaddr",
        @Query("output") output: String = "json"
    ): ReverseGeocodeResponseDto

    @GET("v1/search/local.json")
    suspend fun getPoiFromInputString(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 5,
        @Query("sort") sort: String = "random"
    ): LocalSearchResponseDto
}