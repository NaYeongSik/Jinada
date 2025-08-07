package com.youngsik.jinada.data.impl

import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataStoreRepositoryImpl(val dataStoreDataSource: DataStoreDataSource): DataStoreRepository {
    override suspend fun setUserInfo(nickname: String, uuid: String): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.setUserInfo(nickname,uuid))
    }

    override suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean,isCheckedDailyNoti: Boolean): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.setNotificationEnabled(isCheckedCloserNoti,isCheckedDailyNoti))
    }

    override suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.setRangeOption(closerMemoSearchingRange,closerMemoNotiRange))
    }

    override suspend fun getUserInfo(): Flow<DataResourceResult<Map<String,String>>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.getUserInfo())
    }

    override suspend fun getNotificationEnabled(): Flow<DataResourceResult<Map<String,Boolean>>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.getNotificationEnabled())
    }

    override suspend fun getRangeOption(): Flow<DataResourceResult<Map<String, Float>>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.getRangeOption())
    }
}