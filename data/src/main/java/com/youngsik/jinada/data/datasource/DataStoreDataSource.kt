package com.youngsik.jinada.data.datasource

import com.youngsik.domain.model.DataResourceResult

interface DataStoreDataSource {
    suspend fun setNickname(nickname: String): DataResourceResult<Unit>
    suspend fun setNotificationEnabled(enabled: Boolean): DataResourceResult<Unit>
    suspend fun getNickname(): DataResourceResult<String>
    suspend fun getNotificationEnabled(): DataResourceResult<Boolean>
}