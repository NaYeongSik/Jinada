package com.youngsik.jinada.data.impl

import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.UserInfo
import com.youngsik.domain.model.UserSettings
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import com.youngsik.jinada.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataStoreRepositoryImpl(val dataStoreDataSource: DataStoreDataSource): DataStoreRepository {
    override val userInfo: Flow<UserInfo>
        get() = dataStoreDataSource.userInfoFlow
    override val userSettings: Flow<UserSettings>
        get() = dataStoreDataSource.settingsFlow

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
}