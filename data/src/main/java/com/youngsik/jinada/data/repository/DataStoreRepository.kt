package com.youngsik.jinada.data.repository

import com.youngsik.domain.model.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setUserInfo(nickname: String, uuid: String): Flow<DataResourceResult<Unit>>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean,isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>>
    suspend fun getUserInfo(): Flow<DataResourceResult<Map<String,String>>>
    suspend fun getNotificationEnabled(): Flow<DataResourceResult<Map<String,Boolean>>>
    suspend fun getRangeOption(): Flow<DataResourceResult<Map<String,Float>>>
}