package com.youngsik.domain.usecase

import com.youngsik.domain.repository.NaverRepository
import javax.inject.Inject

/**
 * 좌표(위도, 경도)를 주소 문자열로 변환하는 유즈케이스입니다.
 */
class GetAddressUseCase @Inject constructor(
    private val naverRepository: NaverRepository
) {
    suspend operator fun invoke(
        clientId: String,
        clientSecret: String,
        latitude: Double,
        longitude: Double
    ): String? {
        return naverRepository.getAddressFromCoordinates(
            clientId,
            clientSecret,
            "$longitude, $latitude" // 네이버 API 형식: "경도,위도"
        )
    }
}
