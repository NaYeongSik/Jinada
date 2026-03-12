package com.youngsik.domain.repository

import com.youngsik.domain.entity.PoiItem

/**
 * 네이버 API를 통해 위치 정보를 가져오는 비즈니스 로직을 정의하는 인터페이스입니다.
 */
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
