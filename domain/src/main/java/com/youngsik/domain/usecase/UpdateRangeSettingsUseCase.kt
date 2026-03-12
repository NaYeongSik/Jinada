package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 근접 메모 탐색 범위 및 알림 범위를 설정하는 유즈케이스입니다.
 */
class UpdateRangeSettingsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>> {
        return dataStoreRepository.setRangeOption(closerMemoSearchingRange, closerMemoNotiRange)
    }
}
