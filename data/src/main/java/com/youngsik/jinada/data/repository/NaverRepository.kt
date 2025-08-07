package com.youngsik.jinada.data.repository

import com.youngsik.domain.model.PoiItem

interface NaverRepository {
    suspend fun getAddressFromCoordinates(
        clientId: String,
        clientSecret: String,
        coords: String // "경도,위도" 형태의 문자열
    ): String?

    suspend fun getPoiFromInputString(
        clientId: String,
        clientSecret: String,
        query: String
    ): List<PoiItem>
}