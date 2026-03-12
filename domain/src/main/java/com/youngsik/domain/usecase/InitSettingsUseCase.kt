package com.youngsik.domain.usecase

import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.entity.UserSettings
import com.youngsik.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * 초기 설정 정보(사용자 정보 + 설정)를 로드하는 유즈케이스입니다.
 */
class InitSettingsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Pair<UserInfo, UserSettings>> {
        return combine(
            dataStoreRepository.userInfo,
            dataStoreRepository.userSettings
        ) { userInfo, userSettings ->
            userInfo to userSettings
        }
    }
}
