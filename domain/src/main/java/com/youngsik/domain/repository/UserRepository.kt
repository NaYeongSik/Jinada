package com.youngsik.domain.repository

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 정보 관리와 관련된 비즈니스 규칙을 정의하는 인터페이스입니다.
 */
interface UserRepository {
    suspend fun getUserInfo(uuid: String): Flow<DataResourceResult<UserInfo>>
    suspend fun saveUserInfo(userInfo: UserInfo): Flow<DataResourceResult<Unit>>
    suspend fun isNicknameAvailable(nickname: String): Flow<DataResourceResult<Boolean>>
}
