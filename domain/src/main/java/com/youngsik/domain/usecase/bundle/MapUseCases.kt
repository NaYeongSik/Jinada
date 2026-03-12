package com.youngsik.domain.usecase.bundle

import com.youngsik.domain.usecase.GetAddressUseCase
import com.youngsik.domain.usecase.SearchPoiUseCase
import javax.inject.Inject

/**
 * 지도 API 관련 모든 유즈케이스를 하나로 묶어주는 번들 클래스입니다.
 */
class MapUseCases @Inject constructor(
    val searchPoi: SearchPoiUseCase,
    val getAddress: GetAddressUseCase
)
