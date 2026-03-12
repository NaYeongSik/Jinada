package com.youngsik.domain.usecase

import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 특정 메모를 삭제하는 유즈케이스입니다.
 */
class DeleteMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memoId: String): Flow<DataResourceResult<Unit>> {
        return memoRepository.deleteMemo(memoId)
    }
}
