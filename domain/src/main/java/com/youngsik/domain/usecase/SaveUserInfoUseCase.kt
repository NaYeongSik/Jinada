package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.repository.DataStoreRepository
import com.youngsik.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 사용자 정보를 생성하거나 수정하고, 로컬 설정에도 반영하는 유즈케이스입니다.
 */
class SaveUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    /**
     * Firestore와 DataStore에 사용자 정보를 동시에 저장합니다.
     */
    suspend operator fun invoke(userInfo: UserInfo): Flow<DataResourceResult<Unit>> {
        return userRepository.saveUserInfo(userInfo).flatMapConcat { result ->
            if (result is DataResourceResult.Success) {
                dataStoreRepository.setUserInfo(userInfo.nickname, userInfo.uuid)
            } else flow { emit(result) }
        }
    }
}
