package com.youngsik.jinada.data.datasource

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.UserInfo

interface UserDataSource {
    suspend fun saveUserInfo(userInfo: UserInfo): DataResourceResult<Unit>
    suspend fun getUserInfo(uuid: String): DataResourceResult<UserInfo>
    suspend fun isNicknameAvailable(nickname: String): DataResourceResult<Boolean>
}