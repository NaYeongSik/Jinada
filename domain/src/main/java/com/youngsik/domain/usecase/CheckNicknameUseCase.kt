package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 닉네임 사용 가능 여부를 확인하는 유즈케이스입니다.
 */
class CheckNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String): Flow<DataResourceResult<Boolean>> {
        return userRepository.isNicknameAvailable(nickname)
    }
}
