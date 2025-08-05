package com.youngsik.jinada.data.repository

import com.youngsik.domain.model.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setNickname(nickname: String): Flow<DataResourceResult<Unit>>
    suspend fun setNotificationEnabled(enabled: Boolean): Flow<DataResourceResult<Unit>>
    suspend fun getNickname(): Flow<DataResourceResult<String>>
    suspend fun getNotificationEnabled(): Flow<DataResourceResult<Boolean>>
}