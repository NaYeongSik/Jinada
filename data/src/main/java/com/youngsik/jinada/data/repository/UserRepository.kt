package com.youngsik.jinada.data.repository

import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(uuid: String): Flow<DataResourceResult<UserInfo>>
    suspend fun createUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>>
    suspend fun updateUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>>
    suspend fun isNicknameAvailable(nickname: String): Flow<DataResourceResult<Boolean>>
}