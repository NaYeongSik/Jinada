package com.youngsik.jinada.data.datasource

import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.entity.UserSettings
import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {
    val userInfoFlow: Flow<UserInfo>
    val settingsFlow: Flow<UserSettings>
    suspend fun setUserInfo(nickname: String, uuid: String): DataResourceResult<Unit>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean): DataResourceResult<Unit>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): DataResourceResult<Unit>
}