package com.youngsik.jinada.data.datasource

import com.youngsik.domain.model.DataResourceResult

interface DataStoreDataSource {
    suspend fun setUserInfo(nickname: String, uuid: String): DataResourceResult<Unit>
    suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean): DataResourceResult<Unit>
    suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): DataResourceResult<Unit>
    suspend fun getUserInfo(): DataResourceResult<Map<String,String>>
    suspend fun getNotificationEnabled(): DataResourceResult<Map<String,Boolean>>
    suspend fun getRangeOption(): DataResourceResult<Map<String,Float>>
}