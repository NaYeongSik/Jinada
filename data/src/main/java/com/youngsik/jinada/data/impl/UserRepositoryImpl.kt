package com.youngsik.jinada.data.impl

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.repository.UserRepository
import com.youngsik.jinada.data.datasource.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val userDataSource: UserDataSource): UserRepository {
    override suspend fun getUserInfo(uuid: String): Flow<DataResourceResult<UserInfo>> = flow{
         emit(DataResourceResult.Loading)
         emit(userDataSource.getUserInfo(uuid))
    }.catch { DataResourceResult.Failure(it) }

    override suspend fun saveUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>> = flow{
        emit(DataResourceResult.Loading)
        emit(userDataSource.saveUserInfo(userInfo))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun isNicknameAvailable(nickname: String): Flow<DataResourceResult<Boolean>> = flow{
        emit(DataResourceResult.Loading)
        emit(userDataSource.isNicknameAvailable(nickname))
    }.catch { DataResourceResult.Failure(it) }
}