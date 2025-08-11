package com.youngsik.jinada.data.datasource

import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.UserInfo

interface UserDataSource {
    suspend fun createUserInfo(userInfo: UserInfo): DataResourceResult<Unit>
    suspend fun updateUserInfo(userInfo: UserInfo): DataResourceResult<Unit>
    suspend fun getUserInfo(uuid: String): DataResourceResult<UserInfo>
    suspend fun isNicknameAvailable(nickname: String): DataResourceResult<Boolean>
}