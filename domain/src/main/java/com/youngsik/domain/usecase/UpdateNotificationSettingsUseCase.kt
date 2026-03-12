package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 근접 알림 및 데일리 알림 설정을 업데이트하는 유즈케이스입니다.
 */
class UpdateNotificationSettingsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>> {
        return dataStoreRepository.setNotificationEnabled(isCheckedCloserNoti, isCheckedDailyNoti)
    }
}
