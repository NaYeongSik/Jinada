package com.youngsik.jinada.data.repository

import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.UserInfo
import com.youngsik.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val userInfo: Flow<UserInfo>
    val userSettings: Flow<UserSettings>
    suspend fun setUserInfo(nickname: String, uuid: String): Flow<DataResourceResult<Unit>>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean,isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>>
}