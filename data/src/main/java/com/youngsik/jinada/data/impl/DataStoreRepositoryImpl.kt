package com.youngsik.jinada.data.impl

import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataStoreRepositoryImpl(val dataStoreDataSource: DataStoreDataSource): DataStoreRepository {
    override suspend fun setNickname(nickname: String): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.setNickname(nickname))
    }

    override suspend fun setNotificationEnabled(enabled: Boolean): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.setNotificationEnabled(enabled))
    }

    override suspend fun getNickname(): Flow<DataResourceResult<String>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.getNickname())
    }

    override suspend fun getNotificationEnabled(): Flow<DataResourceResult<Boolean>> = flow{
        emit(DataResourceResult.Loading)
        emit(dataStoreDataSource.getNotificationEnabled())
    }
}