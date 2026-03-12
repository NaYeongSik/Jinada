package com.youngsik.domain.usecase

import com.youngsik.domain.entity.PoiItem
import com.youngsik.domain.repository.NaverRepository
import javax.inject.Inject

/**
 * 입력된 검색어를 바탕으로 장소(POI) 목록을 검색하는 유즈케이스입니다.
 */
class SearchPoiUseCase @Inject constructor(
    private val naverRepository: NaverRepository
) {
    suspend operator fun invoke(
        clientId: String,
        clientSecret: String,
        query: String
    ): List<PoiItem> {
        return naverRepository.getPoiFromInputString(clientId, clientSecret, query)
    }
}
