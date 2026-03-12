package com.youngsik.domain.repository

import com.youngsik.domain.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 실시간 위치 정보 조회를 위한 비즈니스 인터페이스입니다.
 */
interface CurrentLocationRepository {
    val latestLocationState: Flow<LocationEntity?>
    fun updateLatestLocation(location: LocationEntity)
    fun getCurrentLocationUpdates(): Flow<LocationEntity>
    suspend fun getCurrentLocation(): LocationEntity?
}
