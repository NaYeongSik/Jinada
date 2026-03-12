package com.youngsik.domain.repository

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.entity.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * 로컬 설정을 관리하는 비즈니스 로직을 정의하는 인터페이스입니다.
 */
interface DataStoreRepository {
    val userInfo: Flow<UserInfo>
    val userSettings: Flow<UserSettings>
    suspend fun setUserInfo(nickname: String, uuid: String): Flow<DataResourceResult<Unit>>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>>
}
