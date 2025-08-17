package com.youngsik.jinada.data.impl

import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.jinada.data.datasource.UserDataSource
import com.youngsik.jinada.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(val userDataSource: UserDataSource): UserRepository {
    override suspend fun getUserInfo(uuid: String): Flow<DataResourceResult<UserInfo>> = flow{
         emit(DataResourceResult.Loading)
         emit(userDataSource.getUserInfo(uuid))
    }.catch { DataResourceResult.Failure(it) }

    override suspend fun createUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(userDataSource.createUserInfo(userInfo))
    }.catch { DataResourceResult.Failure(it) }

    override suspend fun updateUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(userDataSource.updateUserInfo(userInfo))
    }.catch { DataResourceResult.Failure(it) }

    override suspend fun isNicknameAvailable(nickname: String): Flow<DataResourceResult<Boolean>> = flow{
        emit(DataResourceResult.Loading)
        emit(userDataSource.isNicknameAvailable(nickname))
    }.catch { DataResourceResult.Failure(it) }
}