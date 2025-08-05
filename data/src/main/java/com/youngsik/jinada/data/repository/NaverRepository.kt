package com.youngsik.jinada.data.repository

interface NaverRepository {
    suspend fun getAddressFromCoordinates(
        clientId: String,
        clientSecret: String,
        coords: String // "경도,위도" 형태의 문자열
    ): String?
}