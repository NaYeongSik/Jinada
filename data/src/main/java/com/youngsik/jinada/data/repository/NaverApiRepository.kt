package com.youngsik.jinada.data.repository

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
}