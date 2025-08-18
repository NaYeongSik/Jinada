package com.youngsik.jinada.data.repository

import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.entity.UserSettings
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val userInfo: Flow<UserInfo>
    val userSettings: Flow<UserSettings>
    suspend fun setUserInfo(nickname: String, uuid: String): Flow<DataResourceResult<Unit>>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean,isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>>
}